package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct work of ScryptUtil class.
 */
public class ScryptUtilTest extends AbstractUnitTest {

    /**
     * Tests that generated base64 hash is correct.
     */
    @Test
    public void testBase64HashGenerating() {
        String expectedValue = "lSN80glj5jADRiAyRVCAmj35i74HdKNsNWv128imXns=";
        String inputValue = "063138219";
        assertEquals(expectedValue, ScryptUtil.generateBase64Hash(inputValue));
    }

    /**
     * Tests that generated hex hash is correct.
     */
    @Test
    public void testHexHashGenerating() {
        String expectedValue = "95237cd20963e630034620324550809a3df98bbe0774a36c356bf5dbc8a65e7b";
        String inputValue = "063138219";
        assertEquals(expectedValue, ScryptUtil.generateHexHash(inputValue));
    }
}
