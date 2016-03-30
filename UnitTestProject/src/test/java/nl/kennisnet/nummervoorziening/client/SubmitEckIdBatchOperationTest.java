package nl.kennisnet.nummervoorziening.client;

import org.junit.Test;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Collections;

/**
 * Demonstrates correct usage of the "Submit Eck Id Batch" operation.
 */
public class SubmitEckIdBatchOperationTest extends AbstractUnitTest {

    /**
     * Tests that Nummervoorziening service throws error on invalid Chain Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testSubmitHpgnBatchWithInvalidChainGuid() {
        schoolIdServiceUtil.submitHpgnBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN), INVALID_CHAIN_GUID,
            VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Sector Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testSubmitHpgnBatchWithInvalidSectorGuid() {
        schoolIdServiceUtil.submitHpgnBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN), VALID_CHAIN_GUID,
            INVALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on empty hpgn list.
     */
    @Test(expected = SOAPFaultException.class)
    public void testSubmitHpgnBatchWithEmptyHpgnList() {
        schoolIdServiceUtil.submitHpgnBatch(Collections.emptyMap(), VALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service does not throw error on correct input values.
     */
    @Test
    public void testSimpleSubmitHpgnBatchWithCorrectValues() {
        schoolIdServiceUtil.submitHpgnBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN), VALID_CHAIN_GUID,
            VALID_SECTOR_GUID);
    }
}
