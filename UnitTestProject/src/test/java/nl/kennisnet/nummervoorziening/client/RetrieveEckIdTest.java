package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Test;

import javax.xml.ws.soap.SOAPFaultException;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct usage of the "Retrieve EckId" operation.
 */
public class RetrieveEckIdTest extends AbstractUnitTest {

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
