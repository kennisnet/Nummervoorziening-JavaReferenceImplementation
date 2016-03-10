package nl.kennisnet.nummervoorziening.client;

import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Demonstrates correct usage of the "Ping" operation.
 */
public class PingOperationTest extends AbstractUnitTest {

    /**
     * Tests Nummervoorziening service availability.
     */
    @Test
    public void testGettingAvailability() {
        assertTrue("Web Service is not available", schoolIdServiceUtil.isNummervoorzieningServiceAvailable());
    }

    /**
     * Tests correct version of Nummervoorziening service.
     */
    @Test
    public void testGettingApplicationVersion() {
        String expectedVersion = "0.1.0-SNAPSHOT";

        String applicationVersion = schoolIdServiceUtil.getApplicationVersion();
        assertEquals("Version of Web Service is different from intended version", expectedVersion, applicationVersion);
    }

    /**
     * Tests that time on server is not too different from local time.
     */
    @Test
    public void testSchoolIDDateTime() throws DatatypeConfigurationException {
        long allowedGapInMinutes = 180;

        long serverTimeInMillis = schoolIdServiceUtil.getSystemTime().toGregorianCalendar().getTimeInMillis();
        long localTimeInMillis = System.currentTimeMillis();
        long timeDifference = Math.abs(localTimeInMillis - serverTimeInMillis) / 3600000;

        assertTrue("Time difference is more then 3 hours", timeDifference  < allowedGapInMinutes);
    }
}
