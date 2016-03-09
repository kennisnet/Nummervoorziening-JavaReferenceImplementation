package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.SchoolIDServiceUtil;
import org.junit.Before;
import org.junit.Test;
import school.id.eck.schemas.v1_0.RetrieveSectorsResponse;
import school.id.eck.schemas.v1_0.Sector;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct work with "Retrieve Sectors" operation.
 */
public class RetrieveSectorsTest {

    private SchoolIDServiceUtil schoolIdServiceUtil;

    /**
     * Setups util for working with Web Service.
     */
    @Before
    public void setup() throws NoSuchAlgorithmException, KeyManagementException {
        schoolIdServiceUtil = new SchoolIDServiceUtil();
    }

    /**
     * Tests that Web Service returns non empty list of active sectors.
     */
    @Test
    public void testWebServiceReturnsNonEmptyListOfActiveSectors() {
        RetrieveSectorsResponse retrieveSectorsResponse = schoolIdServiceUtil.retrieveSectors();
        List<Sector> activeSectors = retrieveSectorsResponse.getSector();
        assertFalse("Web Service returned empty list of active sectors!", activeSectors.isEmpty());
    }
}
