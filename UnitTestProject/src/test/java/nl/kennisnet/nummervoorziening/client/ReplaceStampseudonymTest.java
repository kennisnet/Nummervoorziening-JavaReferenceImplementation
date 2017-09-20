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
 * Demonstrates correct usage of the "Replace Stampseudonym" operation.
 */
public class ReplaceStampseudonymTest extends AbstractUnitTest {

    private static final String VALID_HPGN_NEW_PREFIX = "java01";

    private static final String VALID_HPGN_INTERMEDIATE_PREFIX = "java02";

    private static final String VALID_HPGN_OLD_PREFIX = "java03";

    private static int sequenceCounter = 0;

    private static final DateFormat HPGN_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private String validHpgnNew;

    private String validHpgnOld;

    private String validHpgnIntermediate;

    private int getSequentialNumber() {
        return sequenceCounter++;
    }

    /**
     * ﻿ For this test, the PGN is extended with a sequential number and a date string. This is for
     *  demonstration purpose only! On production environment, only PGN with no extensions should be used as the input
     *  for the Scrypt operation.
     */
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
    public void testReplaceStampseudonymWithInvalidNewHpgn() {
        schoolIdServiceUtil.replaceStampseudonym(INVALID_HPGN, validHpgnOld, null);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid old HPgn.
     */
    @Test(expected = SOAPFaultException.class)
    public void testReplaceStampseudonymWithInvalidOldHpgn() {
        schoolIdServiceUtil.replaceStampseudonym(validHpgnNew, INVALID_HPGN, null);
    }

    /**
     * Tests the Substitution functionality based on the output of the
     * generate Stampseudonym functionality. In this case, the substitution
     * should be active immediately.
     */
    @Test
    public void testReplaceStampseudonymNow() {
        // Use the initial dataset to retrieve the Stampseudonym
        String initialStampseudonym = schoolIdServiceUtil.generateStampseudonym(validHpgnOld);

        // Submit the substitution
        String processedStampseudonym = schoolIdServiceUtil.replaceStampseudonym(validHpgnNew, validHpgnOld, null);

        // Retrieve the Stampseudonym based on the new Hpgn, and check the result
        String finalStampseudonym = schoolIdServiceUtil.generateStampseudonym(validHpgnNew);

        // Assert that the Stampseudonym retrieved from the Replace Stampseudonym operation is correct
        assertEquals(initialStampseudonym, processedStampseudonym);

        // Assert that the Stampseudonym retrieved based on the new Hpgn equals the old Hpgn
        assertEquals(initialStampseudonym, finalStampseudonym);
    }

    /**
     * ﻿Tests the Substitution functionality based on the output of the retrieve stampseudonym functionality. In this case,
     * a substitution is submitted to substitution hpgn intermediate to hpgn old, and a second substitution from hpgn new
     * to hpgn intermediate. The last substitution should give back hpgn old instead of hpgn intermediate, also when
     * retrieving the Eck Id based on hpgn new.
     */
    @Test
    public void testReplaceStampseudonymWithIntermediate() {
        // Use the datasets to retrieve the Stampseudonym before substituting
        String oldStampseudonym = schoolIdServiceUtil.generateStampseudonym(validHpgnOld);

        // Submit the substitutions
        String stampseudonymFirstSubstitution = schoolIdServiceUtil.replaceStampseudonym(validHpgnIntermediate,
            validHpgnOld,null);
        String stampseudonymSecondSubstitution = schoolIdServiceUtil.replaceStampseudonym(validHpgnNew,
            validHpgnIntermediate, null);

        // Retrieve the Stampseudonym based on the new Hpgn, and check the result
        String finalStampseudonym = schoolIdServiceUtil.generateStampseudonym(validHpgnNew);

        // Assert that the Stampseudonym retrieved from the first Replace Stampseudonym operation is correct
        assertEquals(oldStampseudonym, stampseudonymFirstSubstitution);

        // Assert that the Stampseudonym retrieved from the second Replace Stampseudonym operation is correct
        assertEquals(oldStampseudonym, stampseudonymSecondSubstitution);

        // Assert that the Stampseudonym retrieved based on the new Hpgn equals the old Hpgn
        assertEquals(oldStampseudonym, finalStampseudonym);
    }

    /**
     * Tests the Substitution functionality based on the output of the retrieve
     * Stampseudonym functionality. In this case, the substitution should not be
     * active immediately.
     */
    @Test
    public void testReplaceStampseudonymFuture() throws DatatypeConfigurationException {
        // Use the future Hpgn to retrieve the Stampseudonym based on the new Hpgn
        String newStampseudonym = schoolIdServiceUtil.generateStampseudonym(validHpgnNew);

        // Set the effective date to a moment in the future
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        gregorianCalendar.setTime(calendar.getTime());
        XMLGregorianCalendar effectiveDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        // Submit the substitution
        String processedStampseudonym = schoolIdServiceUtil.replaceStampseudonym(validHpgnNew, validHpgnOld,
            effectiveDate);

        // Retrieve the Stampseudonym based on the new Hpgn, and check the result
        String finalStampseudonym = schoolIdServiceUtil.generateStampseudonym(validHpgnNew);

        // Assert that the Stampseudonym retrieved from the Replace Stampseudonym operation is correct
        assertEquals(newStampseudonym, processedStampseudonym);

        // Assert that the Stampseudonym retrieved based on the new Hpgn equals the original value (thus not being
        // substituted yet)
        assertEquals(newStampseudonym, finalStampseudonym);
    }
}
