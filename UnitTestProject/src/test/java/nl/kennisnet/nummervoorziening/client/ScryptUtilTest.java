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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Demonstrates correct usage of the ScryptUtil class.
 */
public class ScryptUtilTest extends AbstractUnitTest {

    /**
     * Tests that generated scrypt hash in hexadecimal notation is correct.
     */
    @Test
    public void testStudentHexHashGenerating() {
        String expectedValue = "9735dfd2235eaeb5f0300886bcc99c82ffc1d6420c4e0bde8de7218def2135fa";
        assertEquals(expectedValue, eckIdServiceUtil.getScryptUtil().generateHexHash(VALID_STUDENT_PGN));
    }

    /**
     * Tests that generated scrypt hash in hexadecimal notation is correct.
     */
    @Test
    public void testTeacherHexHashGenerating() {
        String expectedValue = "0b870ff044775ef0360655c40d5b284b7e3ae2b72207a6894794d787eb019e60";
        assertEquals(expectedValue, eckIdServiceUtil.getScryptUtil().generateHexHash(VALID_TEACHER_PGN));
    }

    /**
     * Tests if input is lower-cased internally.
     */
    @Test
    public void testIsInputLowerCased() {
        assertEquals(eckIdServiceUtil.getScryptUtil().generateHexHash("INPUT"), eckIdServiceUtil.getScryptUtil().generateHexHash("input"));
    }

}
