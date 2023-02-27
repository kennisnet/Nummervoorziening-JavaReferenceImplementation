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

import org.junit.Test;

import jakarta.xml.ws.soap.SOAPFaultException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Demonstrates correct usage of the "Submit Stampseudonym Batch" operation.
 */
public class SubmitStampseudonymBatchOperationTest extends AbstractUnitTest {

    /**
     * Tests that Nummervoorziening service throws error on empty HPgn list.
     */
    @Test(expected = SOAPFaultException.class)
    public void testSubmitStampseudonymBatchWithEmptyStampseudonymList() {
        eckIdServiceUtil.submitStampseudonymBatch(Collections.emptyMap());
    }

    /**
     * Tests that Nummervoorziening service does not throw error on correct input values.
     */
    @Test
    public void testSimpleSubmitStampseudonymBatchWithCorrectValues() {
        eckIdServiceUtil.submitStampseudonymBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN));
    }

    /**
     * Tests that Nummervoorziening service does not throw error on batch with more than one element.
     */
    @Test
    public void testSubmitStampseudonymBatchWithMoreThanOneElementWithCorrectValues() {
        Map<Integer, String> input = new HashMap<>();
        input.put(0, VALID_STUDENT_HPGN);
        input.put(1, VALID_TEACHER_HPGN);
        String batchIdentifier = eckIdServiceUtil.submitStampseudonymBatch(input);
        assertNotNull(batchIdentifier);
    }
}
