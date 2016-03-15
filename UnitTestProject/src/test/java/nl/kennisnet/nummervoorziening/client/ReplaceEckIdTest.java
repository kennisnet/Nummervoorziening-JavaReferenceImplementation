package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.soap.SOAPFaultException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct usage of the "Replace Eck (School) ID" operation.
 */
public class ReplaceEckIdTest extends AbstractUnitTest {

    private static final String validHpgnNewPrefix = "java01";

    private static final String validHpgnOldPrefix = "java02";

    private static final String validChainGuid = "e7ec7d3c-c235-4513-bfb6-e54e66854795";

    private static final String validSectorGuid = "512e4729-03a4-43a2-95ba-758071d1b725";

    private static final String invalidHpgn = "";

    private static final String invalidChainGuid = "invalidchainguid";

    private static final String invalidSectorGuid = "invalidsectorguid";

    private static final DateFormat HPGN_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private String validHpgnNew;

    private String validHpgnOld;

    @Before
    public void initValidValues() {
        String dateStr = HPGN_TIMESTAMP_FORMAT.format(new Date());
        validHpgnNew = ScryptUtil.generateHexHash(validHpgnNewPrefix + dateStr);
        validHpgnOld = ScryptUtil.generateHexHash(validHpgnOldPrefix + dateStr);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid new HPgn.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidNewHpgn() {
        schoolIdServiceUtil.replaceEckId(invalidHpgn, validHpgnOld, validChainGuid, validSectorGuid, null);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid old HPgn.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidOldHpgn() {
        schoolIdServiceUtil.replaceEckId(validHpgnNew, invalidHpgn, validChainGuid, validSectorGuid, null);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Chain Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidChain() {
        schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, invalidChainGuid, validSectorGuid, null);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Sector Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidSector() {
        schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, validChainGuid, invalidSectorGuid, null);
    }


    /**
     * Tests the Substitution functionality based on the output of the
     * retrieve Eck ID functionality. In this case, the substitution
     * should be active immediately.
     */
    @Test
    public void testReplaceEckIdNow() {
        // Use the initial dataset to retrieve the Eck ID
        String initialEckId = schoolIdServiceUtil.generateSchoolID(validHpgnOld, validChainGuid, validSectorGuid);

        // Submit the substitution
        String processedEckId = schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, validChainGuid, validSectorGuid, null);

        // Retrieve the Eck ID based on the new Hpgn, and check the result
        String finalEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, validChainGuid, validSectorGuid);

        // Assert that the Eck ID retrieved from the Replace Eck ID operation is correct
        assertEquals(initialEckId, processedEckId);

        // Assert that he Eck ID retrieved based on the new Hpgn equals the old Hpgn
        assertEquals(initialEckId, finalEckId);
    }

    /**
     * Tests the Substitution functionality based on the output of the retrieve
     * Eck ID functionality. In this case, the substitution should not be
     * active immediately.
     */
    @Test
    public void testReplaceEckIdFuture() throws DatatypeConfigurationException {
        // Use a new set of values
        String currentDateStr = HPGN_TIMESTAMP_FORMAT.format(new Date());
        String validFutureHpgnNew = ScryptUtil.generateHexHash(validHpgnNewPrefix + currentDateStr);
        String validFutureHpgnOld = ScryptUtil.generateHexHash(validHpgnOldPrefix + currentDateStr);

        // Use the initial dataset to retrieve the Eck ID
        schoolIdServiceUtil.generateSchoolID(validFutureHpgnOld, validChainGuid, validSectorGuid);

        // Use the future Hpgn to retrieve the Eck ID based on the new Hpgn
        String newEckId = schoolIdServiceUtil.generateSchoolID(validFutureHpgnNew, validChainGuid, validSectorGuid);

        // Set the effective date to a moment in the future
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        gregorianCalendar.setTime(calendar.getTime());
        XMLGregorianCalendar effectiveDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        // Submit the substitution
        String processedEckId = schoolIdServiceUtil.replaceEckId(validFutureHpgnNew, validFutureHpgnOld, validChainGuid,
            validSectorGuid, effectiveDate);

        // Retrieve the Eck ID based on the new Hpgn, and check the result
        String finalEckId = schoolIdServiceUtil.generateSchoolID(validFutureHpgnNew, validChainGuid, validSectorGuid);

        // Assert that the Eck ID retrieved from the Replace Eck ID operation is correct
        assertEquals(newEckId, processedEckId);

        // Assert that he Eck ID retrieved based on the new Hpgn equals the old Hpgn
        assertEquals(newEckId, finalEckId);
    }
}
