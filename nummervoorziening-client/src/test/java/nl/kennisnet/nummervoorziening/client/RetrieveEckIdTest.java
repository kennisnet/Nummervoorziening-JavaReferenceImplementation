package nl.kennisnet.nummervoorziening.client;

import org.junit.Before;
import org.junit.Test;
import school.id.eck.schemas.v1_0.RetrieveEckIdResponse;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct work with "Retrieve EckId" operation.
 */
public class RetrieveEckIdTest {

    private SchoolIdServiceUtil schoolIdServiceUtil;

    /**
     * Setups util for working with Web Service.
     */
    @Before
    public void setup() throws NoSuchAlgorithmException, KeyManagementException {
        schoolIdServiceUtil = new SchoolIdServiceUtil();
    }

    /**
     * Tests that Web Service returns non empty string if we tries to create EckId
     * for first active chain and sector and 'hpgn' string.
     */
    @Test
    public void testWebServiceReturnsNonEmptyEckId() {
        String chainId = schoolIdServiceUtil.retrieveChains().getChain().get(0).getId();
        String sectorId = schoolIdServiceUtil.retrieveSectors().getSector().get(0).getId();
        RetrieveEckIdResponse retrieveEckIdResponse = schoolIdServiceUtil.retrieveEckId(chainId, sectorId, "hpgn");
        assertFalse("Web Service returned empty EckId!", retrieveEckIdResponse.getEckId().getValue().isEmpty());
    }
}
