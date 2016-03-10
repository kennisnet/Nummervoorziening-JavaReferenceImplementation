package nl.kennisnet.nummervoorziening.client;

import org.junit.Test;
import school.id.eck.schemas.v1_0.Sector;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct usage of the "Retrieve Sectors" operation.
 */
public class RetrieveSectorsTest extends AbstractUnitTest {

    /**
     * Tests that the Nummervoorziening service returns non empty list of active sectors.
     */
    @Test
    public void testGettingActiveSectors() {
        List<Sector> activeSectors = schoolIdServiceUtil.getSectors();
        assertFalse("Nummervoorziening service returned empty list of active sectors!", activeSectors.isEmpty());
    }
}
