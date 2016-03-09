package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Test;

import javax.xml.ws.soap.SOAPFaultException;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct work with "Retrieve EckId" operation.
 */
public class RetrieveEckIdTest extends AbstractUnitTest {

    private static final String VALID_PGN = "063138219";

    private static final String VALID_HPGN = ScryptUtil.generateHexHash(VALID_PGN);

    private static  String VALID_CHAIN_GUID = "e7ec7d3c-c235-4513-bfb6-e54e66854795";

    private static  String VALID_SECTOR_GUID = "512e4729-03a4-43a2-95ba-758071d1b725";

    private static final String VALID_EKT_ID = "https://school.id/pilot/998fc3e7c9add25be4369224e18d0876e7598480b184c" +
        "6a35d8f49a49a3649040016f0aab6e292dd7da23292bd2f499e6018dfdab997d9408d80113d6dc72979";

    private static final String INVALID_HPGN = "";

    private static final String INVALID_CHAIN_GUID = "invalidchainguid";

    private static final String INVALID_SECTOR_GUID = "invalidsectorguid";

    /**
     * Tests that Web Service throws error on invalid hpgn.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidHpgn() {
        schoolIdServiceUtil.generateSchoolID(INVALID_HPGN, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Web Service throws error on invalid chain guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidChain() {
        schoolIdServiceUtil.generateSchoolID(VALID_HPGN, INVALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Web Service throws error on invalid sector guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testGetEckIdWithInvalidSector() {
        schoolIdServiceUtil.generateSchoolID(VALID_HPGN, VALID_CHAIN_GUID, INVALID_SECTOR_GUID);
    }

    /**
     * Tests that Web Service returns correct eck_id on valid parameters.
     */
    @Test
    public void testGetEckIdWithValidValues() {
        String schoolId = schoolIdServiceUtil.generateSchoolID(VALID_HPGN, VALID_CHAIN_GUID, VALID_SECTOR_GUID);
        assertEquals(VALID_EKT_ID, schoolId);
    }
}
