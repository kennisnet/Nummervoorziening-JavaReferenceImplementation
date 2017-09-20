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

    protected static final String VALID_STUDENT_STAMPSEUDONYM = "https://id.school/sppilot/d0f58d6544562db32383d9fb" +
        "d7e7d1c6857f9eb8fdaf43db9ac4fac8f3c6897cc6149985fe4a7b91b9be09a11c65b6bfd4d900357b0c96336b5521aaee261cf7";

    protected static final String VALID_TEACHER_STAMPSEUDONYM = "https://id.school/sppilot/e16ce3e75ee460e371972bb5" +
        "b9f0ffee4e6bbdb0d3e0f059f7bf09592a70bb0a5cacf228ca8f1b855f280202c53cf8637c4a911d63969580aaae11ac72a33da4";

    protected static final String VALID_STUDENT_SCHOOL_ID = "https://id.school/pilot/e046daed612e3d3903792c3d7e74b2" +
        "a6b5993cb1b2f6fec6767e41301d526ffec6082a8c2b3e999734eb4cfabb98297111e850bc41fa1f77b6b15c6a7c7d03dc";

    protected static final String VALID_TEACHER_SCHOOL_ID = "https://id.school/pilot/903a199fde822072dc7ebd64a771f1" +
        "e17c3d8223d155e0279d8fd1fe7075b67479521a32c9c2ebbd50c1169b53e3e92cfdda46baf87a54bb9a8314dbd6678424";

    protected static final String INVALID_STAMPSEUDONYM = "";

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
