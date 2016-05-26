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

    protected static final String VALID_STUDENT_SCHOOL_ID = "https://id.school/pilot/4b832082b9763b07864ba2365e14561" +
        "ef66f9e4d6ffa936c4e0d72e444b32160fcf280406202e293a228ddf7e904140bce6b1d17525e73d0e54a57bf920f4ac9";

    protected static final String VALID_TEACHER_SCHOOL_ID = "https://id.school/pilot/d90c55703ea0144590a2c5b2d4f2b55" +
        "b10158c6fb6feb20f8a098a7854810855c4308f8fd2944507f757e98287b4010729fe6b16c30e554a5bc2b9a2207786a3";

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
