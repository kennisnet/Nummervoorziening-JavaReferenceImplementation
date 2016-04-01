package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.SchoolIDBatch;
import org.junit.Test;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Demonstrates correct usage of the "Retrieve Eck Id Batch" operation.
 */
public class RetrieveEckIdBatchOperationTest extends AbstractUnitTest {

    private static final int BATCH_RETRIEVE_ATTEMPTS_COUNT = 10;

    private static final long RETRIEVE_SCHOOL_ID_BATCH_TIMEOUT = 21_000;

    private static final String INVALID_BATCH_IDENTIFIER = "invalid_batch_identifier";

    /**
     * Tests that Nummervoorziening service throws error on invalid batch identifier.
     */
    @Test(expected = SOAPFaultException.class)
    public void testRetrieveEckIdBatchWithInvalidBatchIdentifier() {
        schoolIdServiceUtil.retrieveSchoolIdBatch(INVALID_BATCH_IDENTIFIER);
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves generated School IDs.
     */
    @Test
    public void testSimpleSubmittingAndRetrievingBatch() throws InterruptedException {
        Map<Integer, String> listedHpgnMap = new HashMap<>();
        listedHpgnMap.put(0, VALID_STUDENT_HPGN);
        listedHpgnMap.put(1, VALID_TEACHER_HPGN);
        SchoolIDBatch schoolIDBatch = executeBatchOperation(VALID_CHAIN_GUID, VALID_SECTOR_GUID, listedHpgnMap);
        assertTrue(schoolIDBatch.getFailed().isEmpty());
        Map<Integer, String> generatedValues = schoolIDBatch.getSuccess();
        assertEquals(2, generatedValues.size());
        assertEquals(VALID_STUDENT_SCHOOL_ID, generatedValues.get(0));
        assertEquals(VALID_TEACHER_SCHOOL_ID, generatedValues.get(1));
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves failed items.
     */
    @Test
    public void testRetrievingBatchWithFailedItems() throws InterruptedException {
        Map<Integer, String> listedHpgnMap = new HashMap<>();
        listedHpgnMap.put(0, INVALID_HPGN);
        listedHpgnMap.put(1, INVALID_HPGN);
        SchoolIDBatch schoolIDBatch = executeBatchOperation(VALID_CHAIN_GUID, VALID_SECTOR_GUID, listedHpgnMap);
        Map<Integer, String> failedItems = schoolIDBatch.getFailed();
        assertEquals(2, failedItems.size());
        assertNotEquals(null, failedItems.get(0));
        assertNotEquals(null, failedItems.get(1));
        assertTrue(schoolIDBatch.getSuccess().isEmpty());
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves combination of
     * processed and failed School IDs.
     */
    @Test
    public void testRetrievingBatchWithFailedAndProcessedValues() throws InterruptedException {
        Map<Integer, String> listedHpgnMap = new HashMap<>();
        listedHpgnMap.put(0, VALID_STUDENT_HPGN);
        listedHpgnMap.put(1, INVALID_HPGN);
        SchoolIDBatch schoolIDBatch = executeBatchOperation(VALID_CHAIN_GUID, VALID_SECTOR_GUID, listedHpgnMap);
        Map<Integer, String> failedItems = schoolIDBatch.getFailed();
        assertEquals(1, failedItems.size());
        assertNotEquals(null, failedItems.get(1));
        Map<Integer, String> generatedValues = schoolIDBatch.getSuccess();
        assertEquals(1, generatedValues.size());
        assertEquals(VALID_STUDENT_SCHOOL_ID, generatedValues.get(0));
    }

    /**
     * Tests that Nummervoorziening service throws error on retrieving batch content two times.
     */
    @Test(expected = SOAPFaultException.class)
    public void testRetrieveBatchTwoTimes() throws InterruptedException {
        String batchIdentifier = schoolIdServiceUtil.submitHpgnBatch(
            Collections.singletonMap(0, VALID_STUDENT_HPGN), VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        for (int i = 0; i < BATCH_RETRIEVE_ATTEMPTS_COUNT; i++) {
            Thread.sleep(RETRIEVE_SCHOOL_ID_BATCH_TIMEOUT);
            try {
                schoolIdServiceUtil.retrieveSchoolIdBatch(batchIdentifier);
                break;
            } catch (SOAPFaultException e) {
                // do nothing
            }
        }
        schoolIdServiceUtil.retrieveSchoolIdBatch(batchIdentifier);
    }

    /**
     * Submits batch of hashed PGN values to the server and retrieves result of processing.
     *
     * @param chainGuid     A valid chain guid.
     * @param sectorGuid    A valid sector guid.
     * @param listedHpgnMap Map with hashed PGN values as values and their indexes as keys.
     * @return result of batch processing.
     */
    private SchoolIDBatch executeBatchOperation(String chainGuid, String sectorGuid,
                                                Map<Integer, String> listedHpgnMap) throws InterruptedException {
        String batchIdentifier = schoolIdServiceUtil.submitHpgnBatch(listedHpgnMap, chainGuid, sectorGuid);
        SchoolIDBatch schoolIDBatch = null;
        for (int i = 0; i < BATCH_RETRIEVE_ATTEMPTS_COUNT; i++) {
            Thread.sleep(RETRIEVE_SCHOOL_ID_BATCH_TIMEOUT);
            try {
                schoolIDBatch = schoolIdServiceUtil.retrieveSchoolIdBatch(batchIdentifier);
                break;
            } catch (SOAPFaultException e) {
                // SOAP fault with actor 'NotFinishedException' is thrown if server did not finish processing yet.
                if (!"NotFinishedException".equals(e.getFault().getFaultActor())) {
                    throw e;
                }
            }
        }
        return schoolIDBatch;
    }
}
