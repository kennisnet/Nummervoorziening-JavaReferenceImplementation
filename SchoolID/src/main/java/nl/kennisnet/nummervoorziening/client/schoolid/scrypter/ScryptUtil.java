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
     * Converts input text into hex hash.
     *
     * @param input text for hashing.
     * @return hashed string in hex format.
     */
    public static String generateHexHash(String input) {
        String hashed = scrypt(Constants.SALT, input, Constants.N, Constants.r, Constants.p);
        String[] parts = hashed.split("\\$");
        String generatedHash = parts[4];
        char[] hashedChar = generatedHash.toCharArray();
        return DatatypeConverter.printHexBinary(decode(hashedChar)).toLowerCase();
    }

    /**
     * Converts input text into hash in base64 format.
     *
     * @param input text for hashing.
     * @return hashed string in base64 format.
     */
    public static String generateBase64Hash(String input) {
        String hashed = scrypt(Constants.SALT, input, Constants.N, Constants.r, Constants.p);
        String[] parts = hashed.split("\\$");
        return parts[4];
    }

    private static String scrypt(String saltString, String passwd, int N, int r, int p) {
        try {
            char[] saltChar = saltString.toCharArray();
            byte[] salt = decode(saltChar);
            byte[] derived = SCrypt.scrypt(passwd.toLowerCase().getBytes("UTF-8"), salt, N, r, p, 32);
            String params = Long.toString(log2(N) << 16L | r << 8 | p, 16);
            return "$s0$" + params + '$' + String.valueOf(encode(salt)) + '$' + String.valueOf(encode(derived));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("JVM doesn't support UTF-8?");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("JVM doesn't support SHA1PRNG or HMAC_SHA256?");
        }
    }

    private static int log2(int n) {
        int log = 0;
        if ((n & 0xffff0000) != 0) {
            n >>>= 16;
            log = 16;
        }
        if (n >= 256) {
            n >>>= 8;
            log += 8;
        }
        if (n >= 16) {
            n >>>= 4;
            log += 4;
        }
        if (n >= 4) {
            n >>>= 2;
            log += 2;
        }
        return log + (n >>> 1);
    }
}
