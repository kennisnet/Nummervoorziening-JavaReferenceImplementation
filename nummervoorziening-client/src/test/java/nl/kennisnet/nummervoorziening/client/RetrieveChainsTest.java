package nl.kennisnet.nummervoorziening.client;

import org.junit.Before;
import org.junit.Test;
import school.id.eck.schemas.v1_0.Chain;
import school.id.eck.schemas.v1_0.RetrieveChainsResponse;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct work with "Retrieve Chains" operation.
 */
public class RetrieveChainsTest {

    private SchoolIdServiceUtil schoolIdServiceUtil;

    /**
     * Setups util for working with Web Service.
     */
    @Before
    public void setup() throws NoSuchAlgorithmException, KeyManagementException {
        schoolIdServiceUtil = new SchoolIdServiceUtil();
    }

    /**
     * Tests that Web Service returns non empty list of active chains.
     */
    @Test
    public void testWebServiceReturnsNonEmptyListOfActiveChains() {
        RetrieveChainsResponse retrieveChainsResponse = schoolIdServiceUtil.retrieveChains();
        List<Chain> activeChains = retrieveChainsResponse.getChain();
        assertFalse("Web Service returned empty list of active chains!", activeChains.isEmpty());
    }
}
