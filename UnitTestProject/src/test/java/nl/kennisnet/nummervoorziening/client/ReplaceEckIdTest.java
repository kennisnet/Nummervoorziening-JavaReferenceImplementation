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

    private static final String validHpgnIntermediatePrefix = "java02";

    private static final String validHpgnOldPrefix = "java03";

    private static final String validChainGuid =
        "http://purl.edustandaard.nl/begrippenkader/e7ec7d3c-c235-4513-bfb6-e54e66854795";

    private static final String validSectorGuid =
        "http://purl.edustandaard.nl/begrippenkader/512e4729-03a4-43a2-95ba-758071d1b725";

    private static final String invalidHpgn = "";

    private static final String invalidChainGuid = "invalidchainguid";

    private static final String invalidSectorGuid = "invalidsectorguid";

    private static int sequenceCounter = 0;

    private static final DateFormat HPGN_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private String validHpgnNew;

    private String validHpgnIntermediate;

    private String validHpgnOld;

    private int getSequentialNumber() {
        return sequenceCounter++;
    }

    @Before
    public void initValidValues() {
        String dateStr = HPGN_TIMESTAMP_FORMAT.format(new Date());
        validHpgnNew = ScryptUtil.generateHexHash(validHpgnNewPrefix + getSequentialNumber() + dateStr);
        validHpgnIntermediate = ScryptUtil.generateHexHash(validHpgnIntermediatePrefix +
            getSequentialNumber() + dateStr);
        validHpgnOld = ScryptUtil.generateHexHash(validHpgnOldPrefix + getSequentialNumber() + dateStr);
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
     * ﻿Tests the Substitution functionality based on the output of the retrieve Eck ID functionality. In this case, a
     * substitution is submitted to substitution hpgn intermediate to hpgn old, and a second substitution from hpgn new
     * to hpgn intermediate. The last substitution should give back hpgn old instead of hpgn intermediate, also when
     * retrieving the Eck Id based on hpgn new.
     */
    @Test
    public void testReplaceEckIdWithIntermediate() {
        // Use the datasets to retrieve the Eck IDs before substituting
        String oldEckId = schoolIdServiceUtil.generateSchoolID(validHpgnOld, validChainGuid, validSectorGuid);
        String intermediateEckId = schoolIdServiceUtil.generateSchoolID(validHpgnOld, validChainGuid, validSectorGuid);

        // Submit the substitutions
        String eckIdFirstSubstitution = schoolIdServiceUtil.replaceEckId(validHpgnIntermediate, validHpgnOld,
            validChainGuid, validSectorGuid, null);
        String eckIdSecondSubstitution = schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnIntermediate,
            validChainGuid, validSectorGuid, null);

        // Retrieve the Eck ID based on the new Hpgn, and check the result
        String finalEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, validChainGuid, validSectorGuid);

        // Assert that the Eck ID retrieved from the first Replace Eck ID operation is correct
        assertEquals(oldEckId, eckIdFirstSubstitution);

        // Assert that the Eck ID retrieved from the second Replace Eck ID operation is correct
        assertEquals(oldEckId, eckIdSecondSubstitution);

        // Assert that he Eck ID retrieved based on the new Hpgn equals the old Hpgn
        assertEquals(oldEckId, finalEckId);
    }

    /**
     * Tests the Substitution functionality based on the output of the retrieve
     * Eck ID functionality. In this case, the substitution should not be
     * active immediately.
     */
    @Test
    public void testReplaceEckIdFuture() throws DatatypeConfigurationException {
        // Use the initial dataset to retrieve the Eck ID
        schoolIdServiceUtil.generateSchoolID(validHpgnOld, validChainGuid, validSectorGuid);

        // Use the future Hpgn to retrieve the Eck ID based on the new Hpgn
        String newEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, validChainGuid, validSectorGuid);

        // Set the effective date to a moment in the future
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        gregorianCalendar.setTime(calendar.getTime());
        XMLGregorianCalendar effectiveDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        // Submit the substitution
        String processedEckId = schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, validChainGuid,
            validSectorGuid, effectiveDate);

        // Retrieve the Eck ID based on the new Hpgn, and check the result
        String finalEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, validChainGuid, validSectorGuid);

        // Assert that the Eck ID retrieved from the Replace Eck ID operation is correct
        assertEquals(newEckId, processedEckId);

        // Assert that he Eck ID retrieved based on the new Hpgn equals the old Hpgn
        assertEquals(newEckId, finalEckId);
    }
}
