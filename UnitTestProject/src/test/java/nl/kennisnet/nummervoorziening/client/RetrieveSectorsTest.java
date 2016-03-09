package nl.kennisnet.nummervoorziening.client;

import org.junit.Test;
import school.id.eck.schemas.v1_0.Sector;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct work with "Retrieve Sectors" operation.
 */
public class RetrieveSectorsTest extends AbstractUnitTest {

    /**
     * Tests that Web Service returns non empty list of active sectors.
     */
    @Test
    public void testGettingActiveSectors() {
        List<Sector> activeSectors = schoolIdServiceUtil.getSectors();
        assertFalse("Web Service returned empty list of active sectors!", activeSectors.isEmpty());
    }
}
