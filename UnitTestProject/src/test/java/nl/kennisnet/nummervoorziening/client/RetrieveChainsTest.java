package nl.kennisnet.nummervoorziening.client;

import org.junit.Test;
import school.id.eck.schemas.v1_0.Chain;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Demonstrates correct usage of the "Retrieve Chains" operation.
 */
public class RetrieveChainsTest extends AbstractUnitTest {

    /**
     * Tests that the Nummervoorziening service returns non empty list of active chains.
     */
    @Test
    public void testGettingActiveChains() {
        List<Chain> activeChains = schoolIdServiceUtil.getChains();
        assertFalse("Nummervoorziening service returned empty list of active chains!", activeChains.isEmpty());
    }
}
