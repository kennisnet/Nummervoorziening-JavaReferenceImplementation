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
import school.id.eck.schemas.v1_0.Sector;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct usage of the "Retrieve Sectors" operation.
 */
public class RetrieveSectorsTest extends AbstractUnitTest {

    /**
     * Tests that the Nummervoorziening service returns non empty list of active sectors.
     */
    @Test
    public void testGettingActiveSectors() {
        List<Sector> activeSectors = schoolIdServiceUtil.getSectors();
        assertFalse("Nummervoorziening service returned empty list of active sectors!", activeSectors.isEmpty());
    }
}
