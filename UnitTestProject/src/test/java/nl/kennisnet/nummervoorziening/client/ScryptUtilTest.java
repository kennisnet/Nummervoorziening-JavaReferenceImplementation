package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct usage of the ScryptUtil class.
 */
public class ScryptUtilTest extends AbstractUnitTest {
    private static String INPUT_VALUE = "063138219";

    /**
     * Tests that generated scrypt hash in Base64 notation is correct.
     */
    @Test
    public void testBase64HashGenerating() {
        String expectedValue = "lSN80glj5jADRiAyRVCAmj35i74HdKNsNWv128imXns=";
        assertEquals(expectedValue, ScryptUtil.generateBase64Hash(INPUT_VALUE));
    }

    /**
     * Tests that generated scrypt hash in hexadecimal notation is correct.
     */
    @Test
    public void testHexHashGenerating() {
        String expectedValue = "95237cd20963e630034620324550809a3df98bbe0774a36c356bf5dbc8a65e7b";
        assertEquals(expectedValue, ScryptUtil.generateHexHash(INPUT_VALUE));
    }
}
