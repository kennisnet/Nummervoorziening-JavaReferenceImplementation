package nl.kennisnet.nummervoorziening.client;

import org.junit.Ignore;
import org.junit.Test;
import school.id.eck.schemas.v1_0.PingResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

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
        PingResponse pingResponse = schoolIdServiceUtil.ping();
        assertTrue("Web Service is not available", pingResponse.isAvailable());
    }

    /**
     * Tests correct version of Web Service.
     */
    @Test
    public void testGettingApplicationVersion() {
        PingResponse pingResponse = schoolIdServiceUtil.ping();
        String applicationVersion = pingResponse.getApplicationVersion();
        assertEquals("Version of Web Service is different from intended version", "0.1.0-SNAPSHOT", applicationVersion);
    }

    /**
     * Tests correct timezone configuration. Should we compare timezones?
     */
    @Test
    @Ignore
    public void testWebServiceTimezone() throws DatatypeConfigurationException {
        PingResponse pingResponse = schoolIdServiceUtil.ping();
        XMLGregorianCalendar calendar = pingResponse.getSystemTime();
        int remoteTimezone = calendar.getTimezone();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        int localTimezone = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar).getTimezone();
        assertEquals("Timezone configured not correctly - current server time is " + calendar,
                localTimezone, remoteTimezone);
    }
}
