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

import jakarta.xml.ws.soap.SOAPFaultException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Demonstrates correct usage of the "Submit Eck Id Batch" operation.
 */
public class SubmitEckIdBatchOperationTest extends AbstractUnitTest {

    /**
     * Tests that Nummervoorziening service throws error on invalid Chain Guid.
     */
    @Test
    public void testSubmitEckIdBatchWithInvalidChainGuid() {
        assertThrows(SOAPFaultException.class, () ->
            eckIdServiceUtil.submitEckIdBatch(Collections.singletonMap(0,
                    VALID_STUDENT_STAMPSEUDONYM), INVALID_CHAIN_GUID, VALID_SECTOR_GUID));
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Sector Guid.
     */
    @Test
    public void testSubmitEckIdBatchWithInvalidSectorGuid() {
        assertThrows(SOAPFaultException.class, () ->
            eckIdServiceUtil.submitEckIdBatch(Collections.singletonMap(0,
                    VALID_STUDENT_STAMPSEUDONYM), VALID_CHAIN_GUID, INVALID_SECTOR_GUID));
    }

    /**
     * Tests that Nummervoorziening service throws error on empty Stampseudonym list.
     */
    @Test
    public void testSubmitEckIdBatchWithEmptyStampseudonymList() {
        assertThrows(SOAPFaultException.class, () ->
            eckIdServiceUtil.submitEckIdBatch(Collections.emptyMap(), VALID_CHAIN_GUID, VALID_SECTOR_GUID));
    }

    /**
     * Tests that Nummervoorziening service does not throw error on correct input values.
     */
    @Test
    public void testSimpleSubmitEckIdBatchWithCorrectValues() {
        String batchIdentifier = eckIdServiceUtil.submitEckIdBatch(Collections.singletonMap(0,
            VALID_STUDENT_STAMPSEUDONYM), VALID_CHAIN_GUID, VALID_SECTOR_GUID);

        assertNotNull(batchIdentifier);
    }

    /**
     * Tests that Nummervoorziening service does not throw error on batch with more than one element.
     */
    @Test
    public void testSubmitEckIdBatchWithMoreThanOneElementWithCorrectValues() {
        Map<Integer, String> input = new HashMap<>();
        input.put(0, VALID_STUDENT_STAMPSEUDONYM);
        input.put(1, VALID_TEACHER_STAMPSEUDONYM);
        String batchIdentifier = eckIdServiceUtil.submitEckIdBatch(input, VALID_CHAIN_GUID, VALID_SECTOR_GUID);

        assertNotNull(batchIdentifier);
    }

}
