package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Test;

import javax.xml.ws.soap.SOAPFaultException;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct usage of the "Retrieve EckId" operation.
 */
public class RetrieveEckIdTest extends AbstractUnitTest {

    private static final String VALID_STUDENT_PGN = "063138219";

    private static final String VALID_STUDENT_HPGN = ScryptUtil.generateHexHash(VALID_STUDENT_PGN);

    private static final String VALID_TEACHER_PGN = "20DP teacher@school.com";

    private static final String VALID_TEACHER_HPGN = ScryptUtil.generateHexHash(VALID_TEACHER_PGN);

    private static final String VALID_CHAIN_GUID =
        "http://purl.edustandaard.nl/begrippenkader/e7ec7d3c-c235-4513-bfb6-e54e66854795";

    private static final String VALID_SECTOR_GUID =
        "http://purl.edustandaard.nl/begrippenkader/512e4729-03a4-43a2-95ba-758071d1b725";

    private static final String VALID_STUDENT_SCHOOL_ID = "https://id.school/pilot/a7d5e96cbfc61cddcf9a918150d5137" +
        "c6659497ecb435d97abfc60b7297c750a47a3163af49418acc73148d34915833b1cef077ba687c621aa40654906073571";


    private static final String VALID_TEACHER_SCHOOL_ID = "https://id.school/pilot/8dc3d9adad74ee2d588a6456be26da9" +
        "faab1f0b1801bb15897f0e979ada55556aee041e329b27328259ba383af779080209c5c54f3db9b171bd43980aedc47c3";

    private static final String INVALID_HPGN = "";

    private static final String INVALID_CHAIN_GUID = "invalidchainguid";

    private static final String INVALID_SECTOR_GUID = "invalidsectorguid";

    /**
     * Tests that Nummervoorziening service throws error on invalid HPgn.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidHpgn() {
        schoolIdServiceUtil.generateSchoolID(INVALID_HPGN, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Chain Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidChain() {
        schoolIdServiceUtil.generateSchoolID(VALID_STUDENT_HPGN, INVALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Sector Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidSector() {
        schoolIdServiceUtil.generateSchoolID(VALID_STUDENT_HPGN, VALID_CHAIN_GUID, INVALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service returns correct SchoolID on valid parameters.
     */
    @Test
    public void testGetStudentSchoolIdWithValidValues() {
        String schoolId = schoolIdServiceUtil.generateSchoolID(VALID_STUDENT_HPGN, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        assertEquals(VALID_STUDENT_SCHOOL_ID, schoolId);
    }

    /**
     * Tests that Nummervoorziening service returns correct SchoolID on valid parameters.
     */
    @Test
    public void testGetTeacherSchoolIdWithValidValues() {
        String schoolId = schoolIdServiceUtil.generateSchoolID(VALID_TEACHER_HPGN, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        assertEquals(VALID_TEACHER_SCHOOL_ID, schoolId);
    }
}
