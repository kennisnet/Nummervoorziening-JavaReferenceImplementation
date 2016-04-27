/*
 * Copyright 2016, Stichting Kennisnet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.kennisnet.nummervoorziening.client;

import nl.kennisnet.nummervoorziening.client.schoolid.SchoolIDServiceUtil;
import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import org.junit.Before;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Abstract unit test that initializes SchoolID client before tests.
 */
public abstract class AbstractUnitTest {

    protected static final String VALID_STUDENT_PGN = "063138219";

    protected static final String VALID_STUDENT_HPGN = ScryptUtil.generateHexHash(VALID_STUDENT_PGN);

    protected static final String VALID_TEACHER_PGN = "20DP teacher@school.com";

    protected static final String VALID_TEACHER_HPGN = ScryptUtil.generateHexHash(VALID_TEACHER_PGN);

    protected static final String VALID_CHAIN_GUID =
        "http://purl.edustandaard.nl/begrippenkader/e7ec7d3c-c235-4513-bfb6-e54e66854795";

    protected static final String VALID_SECTOR_GUID =
        "http://purl.edustandaard.nl/begrippenkader/512e4729-03a4-43a2-95ba-758071d1b725";

    protected static final String VALID_STUDENT_SCHOOL_ID = "https://id.school/pilot/a7d5e96cbfc61cddcf9a918150d5137" +
        "c6659497ecb435d97abfc60b7297c750a47a3163af49418acc73148d34915833b1cef077ba687c621aa40654906073571";

    protected static final String VALID_TEACHER_SCHOOL_ID = "https://id.school/pilot/8dc3d9adad74ee2d588a6456be26da9" +
        "faab1f0b1801bb15897f0e979ada55556aee041e329b27328259ba383af779080209c5c54f3db9b171bd43980aedc47c3";

    protected static final String INVALID_HPGN = "";

    protected static final String INVALID_CHAIN_GUID = "invalidchainguid";

    protected static final String INVALID_SECTOR_GUID = "invalidsectorguid";

    protected SchoolIDServiceUtil schoolIdServiceUtil;

    /**
     * Setups Service Util for working with Nummervoorziening service.
     */
    @Before
    public void setup() throws GeneralSecurityException, IOException {
        schoolIdServiceUtil = new SchoolIDServiceUtil();
    }
}
