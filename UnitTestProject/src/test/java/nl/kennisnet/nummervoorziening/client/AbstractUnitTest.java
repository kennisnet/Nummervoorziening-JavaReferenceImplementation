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

import nl.kennisnet.nummervoorziening.client.eckid.EckIDServiceUtil;
import org.junit.Before;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Abstract unit test that initializes the EckID Service client before tests.
 */
public abstract class AbstractUnitTest {

    protected static final String VALID_STUDENT_PGN = "063138219";

    protected static final String VALID_STUDENT_HPGN = "9735dfd2235eaeb5f0300886bcc99c82ffc1d6420c4e0bde8de7218def2135fa";

    protected static final String VALID_TEACHER_PGN = "20DP teacher@school.com";

    protected static final String VALID_TEACHER_HPGN = "0b870ff044775ef0360655c40d5b284b7e3ae2b72207a6894794d787eb019e60";

    protected static final String VALID_CHAIN_GUID =
        "http://purl.edustandaard.nl/begrippenkader/e7ec7d3c-c235-4513-bfb6-e54e66854795";

    protected static final String VALID_SECTOR_GUID =
        "http://purl.edustandaard.nl/begrippenkader/512e4729-03a4-43a2-95ba-758071d1b725";

    protected static final String VALID_STUDENT_STAMPSEUDONYM = "https://ketenid.nl/sppilot/d0f58d6544562db32383d9fb" +
        "d7e7d1c6857f9eb8fdaf43db9ac4fac8f3c6897cc6149985fe4a7b91b9be09a11c65b6bfd4d900357b0c96336b5521aaee261cf7";

    protected static final String VALID_TEACHER_STAMPSEUDONYM = "https://ketenid.nl/sppilot/e16ce3e75ee460e371972bb5" +
        "b9f0ffee4e6bbdb0d3e0f059f7bf09592a70bb0a5cacf228ca8f1b855f280202c53cf8637c4a911d63969580aaae11ac72a33da4";

    protected static final String VALID_STUDENT_ECK_ID = "https://ketenid.nl/pilot/5889fc51565c5488833c42cb2b724c671" +
        "07bb250be8ed99c881c89632adc37b05dd197ee95af1c75be47cb7f9a593ad9ddb2e1f25d2e52c90dbb8dcae4515763";

    protected static final String VALID_TEACHER_ECK_ID = "https://ketenid.nl/pilot/79163b48538cea31249879c2362df75f8" +
        "30b2723fe199fbf11a7881dfc1cd33200705cb195f6997c0a2dd150f9915cfe6f869e8df2513536ffe303088414b1ac";

    protected static final String INVALID_STAMPSEUDONYM = "";

    protected static final String INVALID_HPGN = "";

    protected static final String INVALID_CHAIN_GUID = "invalidchainguid";

    protected static final String INVALID_SECTOR_GUID = "invalidsectorguid";

    protected EckIDServiceUtil eckIdServiceUtil;

    /**
     * Setups Service Util for working with Nummervoorziening service.
     */
    @Before
    public void setup() throws GeneralSecurityException, IOException {
        eckIdServiceUtil = EckIDServiceUtil.EckIDServiceUtilFromConfigFile();
    }
}
