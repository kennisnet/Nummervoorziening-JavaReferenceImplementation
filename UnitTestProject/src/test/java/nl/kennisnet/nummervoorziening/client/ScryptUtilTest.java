package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates correct usage of the ScryptUtil class.
 */
public class ScryptUtilTest extends AbstractUnitTest {

    private static String INPUT_STUDENT_VALUE = "063138219";

    private static String INPUT_TEACHER_VALUE = "20DP teacher@school.com";

    /**
     * Tests that generated scrypt hash in hexadecimal notation is correct.
     */
    @Test
    public void testStudentHexHashGenerating() {
        String expectedValue = "95237cd20963e630034620324550809a3df98bbe0774a36c356bf5dbc8a65e7b";
        assertEquals(expectedValue, ScryptUtil.generateHexHash(INPUT_STUDENT_VALUE));
    }

    /**
     * Tests that generated scrypt hash in hexadecimal notation is correct.
     */
    @Test
    public void testTeacherHexHashGenerating() {
        String expectedValue = "4cadf651ec0197909e6432cb8347369adba39f44276a5b3cd59d17066f10ab3e";
        assertEquals(expectedValue, ScryptUtil.generateHexHash(INPUT_TEACHER_VALUE));
    }

    /**
     * Tests if input is lower-cased internally.
     */
    @Test
    public void testIsInputLowerCased() {
        assertEquals(ScryptUtil.generateHexHash("INPUT"), ScryptUtil.generateHexHash("input"));
    }
}
