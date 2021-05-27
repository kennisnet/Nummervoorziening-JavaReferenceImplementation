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
package nl.kennisnet.nummervoorziening.client.eckid.scrypter;

import com.lambdaworks.crypto.SCrypt;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import static com.lambdaworks.codec.Base64.decode;

/**
 * Util that helps to work with hashes.
 */
public class ScryptUtil {

    /**
     * The first level salt to use for this ScryptUtil instance.
     */
    private String firstLevelSalt = null;

    /**
     * Create a new ScryptUtil helper with the supplied salt.
     *
     * @param salt the salt to use.
     */
    public ScryptUtil(String salt) {
        setSalt(salt);
    }

    /**
     * Converts input text into hex hash. For the sake of standardization and to prevent mismatches, the hexadecimal
     * String is lower cased.
     *
     * @param input text for hashing.
     * @return hashed string in hex format.
     */
    public String generateHexHash(String input) {
        // The input should contain at least one characters
        if (input != null && input.trim().length() > 1) {
            byte[] derived = scrypt(firstLevelSalt, input, Constants.N, Constants.r, Constants.p);
            return DatatypeConverter.printHexBinary(derived).toLowerCase();
        } else {
            throw new IllegalArgumentException("The supplied input doesn't contain at least one character.");
        }
    }

    /**
     * Set the firstLevelsalt to the supplied value.
     *
     * @param salt the salt to set
     */
    private void setSalt(String salt) {
        if (salt == null || salt.trim().length() < 1) {
            throw new IllegalArgumentException("The first level salt is not set properly.");
        }
        firstLevelSalt = salt;
    }

    /**
     * Returns a scrypted hash based on the supplied arguments.

     * @param saltString the salt
     * @param passwd the password
     * @param N CPU cost parameter.
     * @param r Memory cost parameter.
     * @param p Parallelization parameter.
     *
     * @return scrypted hash.
     */
    private static byte[] scrypt(String saltString, String passwd, int N, int r, int p) {
        try {
            char[] saltChar = saltString.toCharArray();
            byte[] salt = decode(saltChar);
            return SCrypt.scrypt(passwd.toLowerCase().getBytes(StandardCharsets.UTF_8), salt, N, r, p, 32);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("JVM doesn't support SHA1PRNG or HMAC_SHA256?", e);
        }
    }
}
