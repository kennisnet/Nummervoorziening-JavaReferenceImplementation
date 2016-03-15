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

    private static  String VALID_CHAIN_GUID = "e7ec7d3c-c235-4513-bfb6-e54e66854795";

    private static  String VALID_SECTOR_GUID = "512e4729-03a4-43a2-95ba-758071d1b725";

    private static final String VALID_STUDENT_SCHOOL_ID = "https://id.school/pilot/998fc3e7c9add25be4369224e18d0876" +
        "e7598480b184c6a35d8f49a49a3649040016f0aab6e292dd7da23292bd2f499e6018dfdab997d9408d80113d6dc72979";

    private static final String VALID_TEACHER_SCHOOL_ID = "https://id.school/pilot/2650076c96066464e76063f92c6dd59" +
        "c46bca515d9e7c0c8dd9ae1c1b733751a3ab20b50688b39dc633a04dbefc76ac2bbbd9e62abe3b68558dbbcb831148d62";

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
