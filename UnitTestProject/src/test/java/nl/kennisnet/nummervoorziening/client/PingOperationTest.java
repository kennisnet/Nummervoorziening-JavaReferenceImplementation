package nl.kennisnet.nummervoorziening.client;

import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Demonstrates correct work with "Ping" operation.
 */
public class PingOperationTest extends AbstractUnitTest {

    /**
     * Tests Web Service availability.
     */
    @Test
    public void testGettingAvailability() {
        assertTrue("Web Service is not available", schoolIdServiceUtil.isWebServiceAvailable());
    }

    /**
     * Tests correct version of Web Service.
     */
    @Test
    public void testGettingApplicationVersion() {
        String applicationVersion = schoolIdServiceUtil.getApplicationVersion();
        assertEquals("Version of Web Service is different from intended version", "0.1.0-SNAPSHOT", applicationVersion);
    }

    /**
     * Tests that time on server is not different from local time.
     */
    @Test
    public void testWebServiceTimezone() throws DatatypeConfigurationException {
        long serverTimeInMillis = schoolIdServiceUtil.getSystemTime().toGregorianCalendar().getTimeInMillis();
        long localTimeInMillis = System.currentTimeMillis();
        long timeDifference = Math.abs(localTimeInMillis - serverTimeInMillis);
        assertTrue("Time difference is more then 2 minutes", timeDifference  < 120_000);
    }
}
