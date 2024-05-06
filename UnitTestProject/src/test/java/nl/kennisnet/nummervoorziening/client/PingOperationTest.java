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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Demonstrates correct usage of the "Ping" operation.
 */
public class PingOperationTest extends AbstractUnitTest {

    /**
     * Tests Nummervoorziening service availability.
     */
    @Test
    public void testGettingAvailability() {
        assertTrue(eckIdServiceUtil.isNummervoorzieningServiceAvailable(), "Web Service is not available");
    }

    /**
     * Tests correct version of Nummervoorziening service.
     */
    @Test
    public void testGettingApplicationVersion() {
        String expectedVersion = "2.0";

        String applicationVersion = eckIdServiceUtil.getApplicationVersion();
        assertTrue(applicationVersion.startsWith(expectedVersion), "Version of Web Service is different from intended version");
    }

    /**
     * Tests that time on server is not too different from local time.
     */
    @Test
    public void testEckIdDateTime() {
        long allowedGapInMinutes = 180;

        long serverTimeInMillis = eckIdServiceUtil.getSystemTime().toGregorianCalendar().getTimeInMillis();
        long localTimeInMillis = System.currentTimeMillis();
        long timeDifference = Math.abs(localTimeInMillis - serverTimeInMillis) / 3600000;

        assertTrue(timeDifference  < allowedGapInMinutes, "Time difference is more then 3 hours");
    }

}
