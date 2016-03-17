package nl.kennisnet.nummervoorziening.client.schoolid.scrypter;

import com.lambdaworks.crypto.SCrypt;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static com.lambdaworks.codec.Base64.decode;
import static com.lambdaworks.codec.Base64.encode;

/**
 * Util that helps to work with hashes.
 */
public class ScryptUtil {

    /**
     * Static class, should not be instantiated.
     */
    private ScryptUtil() { }

    /**
     * Converts input text into hex hash. For the sake of standardization and to prevent mismatches, the hexadecimal
     * String is lower cased.
     *
     * @param input text for hashing.
     * @return hashed string in hex format.
     */
    public static String generateHexHash(String input) {
        // The input should contain at least one characters
        if (input != null && input.trim().length() > 1) {
            byte[] derived = scrypt(Constants.SALT, input, Constants.N, Constants.r, Constants.p);
            return DatatypeConverter.printHexBinary(derived).toLowerCase();
        } else {
            throw new IllegalArgumentException("The supplied input doesn't contain at least one character.");
        }
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
            return SCrypt.scrypt(passwd.toLowerCase().getBytes("UTF-8"), salt, N, r, p, 32);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("JVM doesn't support UTF-8?", e);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("JVM doesn't support SHA1PRNG or HMAC_SHA256?", e);
        }
    }
}
