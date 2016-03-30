package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Test;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Collections;

/**
 * Demonstrates correct usage of the "Submit Eck Id Batch" operation.
 */
public class SubmitEckIdBatchOperationTest extends AbstractUnitTest {

    private static final String VALID_STUDENT_PGN = "063138219";

    private static final String VALID_STUDENT_HPGN = ScryptUtil.generateHexHash(VALID_STUDENT_PGN);

    private static final String VALID_CHAIN_GUID =
        "http://purl.edustandaard.nl/begrippenkader/e7ec7d3c-c235-4513-bfb6-e54e66854795";

    private static final String VALID_SECTOR_GUID =
        "http://purl.edustandaard.nl/begrippenkader/512e4729-03a4-43a2-95ba-758071d1b725";

    private static final String INVALID_CHAIN_GUID = "invalidchainguid";

    private static final String INVALID_SECTOR_GUID = "invalidsectorguid";

    /**
     * Tests that Nummervoorziening service throws error on invalid Chain Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testSubmitEckIdBatchWithInvalidChainGuid() {
        schoolIdServiceUtil.submitSchoolIdBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN), INVALID_CHAIN_GUID,
            VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on invalid Sector Guid.
     */
    @Test(expected = SOAPFaultException.class)
    public void testSubmitEckIdBatchWithInvalidSectorGuid() {
        schoolIdServiceUtil.submitSchoolIdBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN), VALID_CHAIN_GUID,
            INVALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service throws error on empty hpgn list.
     */
    @Test(expected = SOAPFaultException.class)
    public void testSubmitEckIdBatchWithEmptyHpgnList() {
        schoolIdServiceUtil.submitSchoolIdBatch(Collections.emptyMap(), VALID_CHAIN_GUID, VALID_SECTOR_GUID);
    }

    /**
     * Tests that Nummervoorziening service does not throw error on correct input values.
     */
    @Test
    public void testSimpleSubmitEckIdBatchWithCorrectValues() {
        schoolIdServiceUtil.submitSchoolIdBatch(Collections.singletonMap(0, VALID_STUDENT_HPGN), VALID_CHAIN_GUID,
            VALID_SECTOR_GUID);
    }
}
