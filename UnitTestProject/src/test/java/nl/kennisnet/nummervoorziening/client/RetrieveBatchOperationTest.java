/*
 * Copyright 2016, Stichting Kennisnet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.eckid.EckIDServiceBatch;
import org.junit.Test;

import jakarta.xml.ws.soap.SOAPFaultException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Demonstrates correct usage of the "Retrieve Batch" operation for ECK-ID and stampseudonym batches.
 */
public class RetrieveBatchOperationTest extends AbstractUnitTest {

    private static final int BATCH_RETRIEVE_ATTEMPTS_COUNT = 10;

    private static final long RETRIEVE_ECK_ID_BATCH_TIMEOUT = 21_000;

    private static final String INVALID_BATCH_IDENTIFIER = "invalid_batch_identifier";

    /**
     * Tests that Nummervoorziening service throws error on invalid batch identifier.
     */
    @Test(expected = SOAPFaultException.class)
    public void testRetrieveBatchWithInvalidBatchIdentifier() {
        eckIdServiceUtil.retrieveEckIDBatch(INVALID_BATCH_IDENTIFIER);
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves generated EckIDs.
     */
    @Test
    public void testSimpleSubmittingAndRetrievingEckIdBatch() throws InterruptedException {
        Map<Integer, String> listedStampseudonymMap = new HashMap<>();
        listedStampseudonymMap.put(0, VALID_STUDENT_STAMPSEUDONYM);
        listedStampseudonymMap.put(1, VALID_TEACHER_STAMPSEUDONYM);
        EckIDServiceBatch eckIDServiceBatch =
            executeEckIdBatchOperation(VALID_CHAIN_GUID, VALID_SECTOR_GUID, listedStampseudonymMap);

        assertTrue(eckIDServiceBatch.getFailed().isEmpty());
        Map<Integer, String> generatedValues = eckIDServiceBatch.getSuccess();

        assertEquals(2, generatedValues.size());
        assertEquals(VALID_STUDENT_ECK_ID, generatedValues.get(0));
        assertEquals(VALID_TEACHER_ECK_ID, generatedValues.get(1));
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves generated stampseudonyms.
     */
    @Test
    public void testSimpleSubmittingAndRetrievingStampseudonymBatch() throws InterruptedException {
        Map<Integer, String> listedHPgnMap = new HashMap<>();
        listedHPgnMap.put(0, VALID_STUDENT_HPGN);
        listedHPgnMap.put(1, VALID_TEACHER_HPGN);
        EckIDServiceBatch eckIDServiceBatch = executeStampseudonymBatchOperation(listedHPgnMap);

        assertTrue(eckIDServiceBatch.getFailed().isEmpty());
        Map<Integer, String> generatedValues = eckIDServiceBatch.getSuccess();

        assertEquals(2, generatedValues.size());
        assertEquals(VALID_STUDENT_STAMPSEUDONYM, generatedValues.get(0));
        assertEquals(VALID_TEACHER_STAMPSEUDONYM, generatedValues.get(1));
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves failed items for an EckID batch.
     */
    @Test
    public void testRetrievingEckIdBatchWithFailedItems() throws InterruptedException {
        Map<Integer, String> listedStampseudonymMap = new HashMap<>();
        listedStampseudonymMap.put(0, INVALID_STAMPSEUDONYM);
        listedStampseudonymMap.put(1, INVALID_STAMPSEUDONYM);
        EckIDServiceBatch eckIDServiceBatch =
            executeEckIdBatchOperation(VALID_CHAIN_GUID, VALID_SECTOR_GUID, listedStampseudonymMap);

        Map<Integer, String> failedItems = eckIDServiceBatch.getFailed();

        assertEquals(2, failedItems.size());
        assertNotEquals(null, failedItems.get(0));
        assertNotEquals(null, failedItems.get(1));
        assertTrue(eckIDServiceBatch.getSuccess().isEmpty());
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves failed items for stampseudonym batch.
     */
    @Test
    public void testRetrievingStampseudonymBatchWithFailedItems() throws InterruptedException {
        Map<Integer, String> listedHPgnMap = new HashMap<>();
        listedHPgnMap.put(0, INVALID_HPGN);
        listedHPgnMap.put(1, INVALID_HPGN);
        EckIDServiceBatch eckIDServiceBatch = executeStampseudonymBatchOperation(listedHPgnMap);

        Map<Integer, String> failedItems = eckIDServiceBatch.getFailed();

        assertEquals(2, failedItems.size());
        assertNotEquals(null, failedItems.get(0));
        assertNotEquals(null, failedItems.get(1));
        assertTrue(eckIDServiceBatch.getSuccess().isEmpty());
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves combination of
     * processed and failed EckIDs for an EckID batch.
     */
    @Test
    public void testRetrievingEckIdBatchWithFailedAndProcessedValues() throws InterruptedException {
        Map<Integer, String> listedStampseudonymMap = new HashMap<>();
        listedStampseudonymMap.put(0, VALID_STUDENT_STAMPSEUDONYM);
        listedStampseudonymMap.put(1, INVALID_STAMPSEUDONYM);
        EckIDServiceBatch eckIDServiceBatch =
            executeEckIdBatchOperation(VALID_CHAIN_GUID, VALID_SECTOR_GUID, listedStampseudonymMap);

        Map<Integer, String> failedItems = eckIDServiceBatch.getFailed();
        assertEquals(1, failedItems.size());
        assertNotEquals(null, failedItems.get(1));

        Map<Integer, String> generatedValues = eckIDServiceBatch.getSuccess();
        assertEquals(1, generatedValues.size());
        assertEquals(VALID_STUDENT_ECK_ID, generatedValues.get(0));
    }

    /**
     * Tests that Nummervoorziening service correctly retrieves combination of
     * processed and failed Stampseudonyms for a Stampseudonym batch.
     */
    @Test
    public void testRetrievingStampseudonymBatchWithFailedAndProcessedValues() throws InterruptedException {
        Map<Integer, String> listedHPgnMap = new HashMap<>();
        listedHPgnMap.put(0, VALID_STUDENT_HPGN);
        listedHPgnMap.put(1, INVALID_HPGN);
        EckIDServiceBatch eckIDServiceBatch = executeStampseudonymBatchOperation(listedHPgnMap);

        Map<Integer, String> failedItems = eckIDServiceBatch.getFailed();
        assertEquals(1, failedItems.size());
        assertNotEquals(null, failedItems.get(1));

        Map<Integer, String> generatedValues = eckIDServiceBatch.getSuccess();
        assertEquals(1, generatedValues.size());
        assertEquals(VALID_STUDENT_STAMPSEUDONYM, generatedValues.get(0));
    }

    /**
     * Tests that Nummervoorziening service throws error on retrieving batch content two times for an EckID batch.
     */
    @Test(expected = SOAPFaultException.class)
    public void testRetrieveEckIdBatchTwoTimes() throws InterruptedException {
        String batchIdentifier = eckIdServiceUtil.submitEckIdBatch(
            Collections.singletonMap(0, VALID_STUDENT_STAMPSEUDONYM), VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        for (int i = 0; i < BATCH_RETRIEVE_ATTEMPTS_COUNT; i++) {
            Thread.sleep(RETRIEVE_ECK_ID_BATCH_TIMEOUT);
            try {
                eckIdServiceUtil.retrieveEckIDBatch(batchIdentifier);
                break;
            } catch (SOAPFaultException e) {
                // do nothing
            }
        }
        eckIdServiceUtil.retrieveEckIDBatch(batchIdentifier);
    }

    /**
     * Tests that Nummervoorziening service throws error on retrieving batch content two times for stampseudonym batch.
     */
    @Test(expected = SOAPFaultException.class)
    public void testRetrieveStampseudonymBatchTwoTimes() throws InterruptedException {
        String batchIdentifier = eckIdServiceUtil.submitStampseudonymBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN));
        for (int i = 0; i < BATCH_RETRIEVE_ATTEMPTS_COUNT; i++) {
            Thread.sleep(RETRIEVE_ECK_ID_BATCH_TIMEOUT);
            try {
                eckIdServiceUtil.retrieveEckIDBatch(batchIdentifier);
                break;
            } catch (SOAPFaultException e) {
                // do nothing
            }
        }
        eckIdServiceUtil.retrieveEckIDBatch(batchIdentifier);
    }

    /**
     * Submits batch of Stampseudonym values to the server and retrieves result of processing.
     *
     * @param chainGuid              A valid chain guid.
     * @param sectorGuid             A valid sector guid.
     * @param listedStampseudonymMap Map with Stampseudonym values as values and their indexes as keys.
     * @return result of batch processing.
     */
    private EckIDServiceBatch executeEckIdBatchOperation(String chainGuid, String sectorGuid,
                                             Map<Integer, String> listedStampseudonymMap) throws InterruptedException {

        String batchIdentifier =
            eckIdServiceUtil.submitEckIdBatch(listedStampseudonymMap, chainGuid, sectorGuid);
        return retrieveSubmittedBatch(batchIdentifier);
    }

    /**
     * Submits batch of HPgn values to the server and retrieves result of processing.
     *
     * @param listedHPgnMap Map with Stampseudonym values as values and their indexes as keys.
     * @return result of batch processing.
     */
    private EckIDServiceBatch executeStampseudonymBatchOperation(Map<Integer, String> listedHPgnMap) throws InterruptedException {
        String batchIdentifier = eckIdServiceUtil.submitStampseudonymBatch(listedHPgnMap);
        return retrieveSubmittedBatch(batchIdentifier);
    }

    private EckIDServiceBatch retrieveSubmittedBatch(String batchIdentifier) throws InterruptedException {
        EckIDServiceBatch eckIDServiceBatch = null;
        for (int i = 0; i < BATCH_RETRIEVE_ATTEMPTS_COUNT; i++) {
            Thread.sleep(RETRIEVE_ECK_ID_BATCH_TIMEOUT);
            try {
                eckIDServiceBatch = eckIdServiceUtil.retrieveEckIDBatch(batchIdentifier);
                break;
            } catch (SOAPFaultException e) {
                // SOAP fault with actor 'NotFinishedException' is thrown if server did not finish processing yet.
                if (!"NotFinishedException".equals(e.getFault().getFaultActor())) {
                    throw e;
                }
            }
        }
        return eckIDServiceBatch;
    }
}
