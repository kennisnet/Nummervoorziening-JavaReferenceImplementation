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

    private static final String VALID_HPGN_NEW_PREFIX = "java01";

    private static final String VALID_HPGN_INTERMEDIATE_PREFIX = "java02";

    private static final String VALID_HPGN_OLD_PREFIX = "java03";

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
        validHpgnNew = ScryptUtil.generateHexHash(VALID_HPGN_NEW_PREFIX + getSequentialNumber() + dateStr);
        validHpgnIntermediate = ScryptUtil.generateHexHash(VALID_HPGN_INTERMEDIATE_PREFIX +
            getSequentialNumber() + dateStr);
        validHpgnOld = ScryptUtil.generateHexHash(VALID_HPGN_OLD_PREFIX + getSequentialNumber() + dateStr);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid new HPgn.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidNewHpgn() {
        schoolIdServiceUtil.replaceEckId(INVALID_HPGN, validHpgnOld, VALID_CHAIN_GUID, VALID_SECTOR_GUID, null);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid old HPgn.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidOldHpgn() {
        schoolIdServiceUtil.replaceEckId(validHpgnNew, INVALID_HPGN, VALID_CHAIN_GUID, VALID_SECTOR_GUID, null);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Chain Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidChain() {
        schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, INVALID_SECTOR_GUID, VALID_SECTOR_GUID, null);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Sector Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceEckIdWithInvalidSector() {
        schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, VALID_CHAIN_GUID, INVALID_SECTOR_GUID, null);
    }


    /**
     * Tests the Substitution functionality based on the output of the
     * retrieve Eck ID functionality. In this case, the substitution
     * should be active immediately.
     */
    @Test
    public void testReplaceEckIdNow() {
        // Use the initial dataset to retrieve the Eck ID
        String initialEckId = schoolIdServiceUtil.generateSchoolID(validHpgnOld, VALID_CHAIN_GUID, VALID_SECTOR_GUID);

        // Submit the substitution
        String processedEckId = schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, VALID_CHAIN_GUID, VALID_SECTOR_GUID, null);

        // Retrieve the Eck ID based on the new Hpgn, and check the result
        String finalEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, VALID_CHAIN_GUID, VALID_SECTOR_GUID);

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
        String oldEckId = schoolIdServiceUtil.generateSchoolID(validHpgnOld, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        String intermediateEckId = schoolIdServiceUtil.generateSchoolID(validHpgnOld, VALID_CHAIN_GUID, VALID_SECTOR_GUID);

        // Submit the substitutions
        String eckIdFirstSubstitution = schoolIdServiceUtil.replaceEckId(validHpgnIntermediate, validHpgnOld,
            VALID_CHAIN_GUID, VALID_SECTOR_GUID, null);
        String eckIdSecondSubstitution = schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnIntermediate,
            VALID_CHAIN_GUID, VALID_SECTOR_GUID, null);

        // Retrieve the Eck ID based on the new Hpgn, and check the result
        String finalEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, VALID_CHAIN_GUID, VALID_SECTOR_GUID);

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
        // Use the future Hpgn to retrieve the Eck ID based on the new Hpgn
        String newEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, VALID_CHAIN_GUID, VALID_SECTOR_GUID);

        // Set the effective date to a moment in the future
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        gregorianCalendar.setTime(calendar.getTime());
        XMLGregorianCalendar effectiveDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        // Submit the substitution
        String processedEckId = schoolIdServiceUtil.replaceEckId(validHpgnNew, validHpgnOld, VALID_CHAIN_GUID,
            VALID_SECTOR_GUID, effectiveDate);

        // Retrieve the Eck ID based on the new Hpgn, and check the result
        String finalEckId = schoolIdServiceUtil.generateSchoolID(validHpgnNew, VALID_CHAIN_GUID, VALID_SECTOR_GUID);

        // Assert that the Eck ID retrieved from the Replace Eck ID operation is correct
        assertEquals(newEckId, processedEckId);

        // Assert that the Eck ID retrieved based on the new Hpgn equals the original value (thus not being substituted)
        assertEquals(newEckId, finalEckId);
    }
}
