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

import javax.xml.ws.soap.SOAPFaultException;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct usage of the "Retrieve EckId" operation.
 */
public class RetrieveEckIdOperationTest extends AbstractUnitTest {

    /**
     * Tests that Nummervoorziening service throws error on invalid Stampseudonym.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidStampseudonym() {
        schoolIdServiceUtil.generateSchoolID(INVALID_STAMPSEUDONYM, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Chain Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidChain() {
        schoolIdServiceUtil.generateSchoolID(VALID_STUDENT_STAMPSEUDONYM, INVALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Sector Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidSector() {
        schoolIdServiceUtil.generateSchoolID(VALID_STUDENT_STAMPSEUDONYM, VALID_CHAIN_GUID, INVALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service returns correct SchoolID on valid parameters.
     */
    @Test
    public void testGetStudentSchoolIdWithValidValues() {
        String schoolId = schoolIdServiceUtil.generateSchoolID(VALID_STUDENT_STAMPSEUDONYM, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        assertEquals(VALID_STUDENT_SCHOOL_ID, schoolId);
    }

    /**
     * Tests that Nummervoorziening service returns correct SchoolID on valid parameters.
     */
    @Test
    public void testGetTeacherSchoolIdWithValidValues() {
        String schoolId = schoolIdServiceUtil.generateSchoolID(VALID_TEACHER_STAMPSEUDONYM, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        assertEquals(VALID_TEACHER_SCHOOL_ID, schoolId);
    }

    /**
     * Tests the retrieve EckId functionality based on the output of the
     * retrieve Stampseudonym functionality for student.
     */
    @Test
    public void testRetrieveStudentSchoolIdAfterRetrieveStampseudonym() {
        // Use the hPGN to retrieve the Stampseudonym
        String stampseudonym = schoolIdServiceUtil.generateStampseudonym(VALID_STUDENT_HPGN);

        // Retrieve the Eck ID based on retrieved Stampseudonym, and check the result
        String schoolId = schoolIdServiceUtil.generateSchoolID(stampseudonym, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        assertEquals(VALID_STUDENT_SCHOOL_ID, schoolId);
    }

    /**
     * Tests the retrieve EckId functionality based on the output of the
     * retrieve Stampseudonym functionality for teacher.
     */
    @Test
    public void testRetrieveTeacherSchoolIdAfterRetrieveStampseudonym() {
        // Use the hPGN to retrieve the Stampseudonym
        String stampseudonym = schoolIdServiceUtil.generateStampseudonym(VALID_TEACHER_HPGN);

        // Retrieve the Eck ID based on retrieved Stampseudonym, and check the result
        String schoolId = schoolIdServiceUtil.generateSchoolID(stampseudonym, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        assertEquals(VALID_TEACHER_SCHOOL_ID, schoolId);
    }
}
