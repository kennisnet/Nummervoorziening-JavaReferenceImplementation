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
import school.id.eck.schemas.v1_0.Chain;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct usage of the "Retrieve Chains" operation.
 */
public class RetrieveChainsOperationTest extends AbstractUnitTest {

    /**
     * Tests that the Nummervoorziening service returns non empty list of active chains.
     */
    @Test
    public void testGettingActiveChains() {
        List<Chain> activeChains = schoolIdServiceUtil.getChains();
        assertFalse("Nummervoorziening service returned empty list of active chains!", activeChains.isEmpty());
    }
}
