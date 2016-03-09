package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.SchoolIDServiceUtil;
import org.junit.Before;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Abstract unit test that initializes SchoolID client before tests.
 */
public class AbstractUnitTest {

    protected SchoolIDServiceUtil schoolIdServiceUtil;

    /**
     * Setups util for working with Web Service.
     */
    @Before
    public void setup() throws NoSuchAlgorithmException, KeyManagementException {
        schoolIdServiceUtil = new SchoolIDServiceUtil();
    }
}
