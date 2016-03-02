/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrypter;

import com.lambdaworks.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static com.lambdaworks.codec.Base64.*;
/**
 *
 * @author Beekman.Thomas
 */
public class SCrypter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int N = 16384;
        int r = 8;
        int p = 1;
        String passwd = "secret";
        String salt = "rktYml0MIp9TC9u6Ny6uqw==";
        
        if (args.length == 0) {    
            System.err.println("SCrypter v0.31-20160302 (T.Beekman@Kennisnet.nl)\n");
            System.err.println("Usage: java -jar SCrypter.jar N:[N] r:[r] p:[p] passwd:[passwd] salt:[salt]");
            System.err.println("N:\tGeneral work factor, iteration count (default: 16384)");
            System.err.println("r:\tBlocksize in use for underlying hash; fine-tunes the relative memory-cost (default: 8)");
            System.err.println("p:\tParallelization factor; fine-tunes the relative cpu-cost (default: 1)");
            System.err.println("passwd:\tThe input for scrypt (default: secret)");
            System.err.println("salt:\tThe salt - base64 encoded (default: 16384)\n");        
            System.err.println("Example: java -jar scrypter N:16384 r:8 p:1 passwd:secret salt:rktYml0MIp9TC9u6Ny6uqw==\n");        
        } else {
            for (String arg : args) {
                String[] argParts = arg.split(":");                

                if (argParts.length == 2) {                            
                    switch (argParts[0].toLowerCase()) {
                        case "n":
                            N = Integer.parseInt(argParts[1]);
                            break;
                        case "r":
                            r = Integer.parseInt(argParts[1]);
                            break;
                        case "p":
                            p = Integer.parseInt(argParts[1]);
                            break;
                        case "passwd":
                            passwd = argParts[1];
                            break;
                        case "salt":
                            salt = argParts[1];
                            break;                        
                    }
                }
            }
        } 
        
        System.err.println("Used arguments: N:" + N + ", r:" + r + ", p:" + p +
                ", passwd:" + passwd + ", salt:" + salt + "\n");
        
        // Do the magic, and log the processing time
        long startTime = System.currentTimeMillis();
        String hashed = scrypt(salt, passwd, N, r, p);
        long endTime = System.currentTimeMillis();        
        String[] parts = hashed.split("\\$");
        
        String generatedHash = parts[4];
        char[] hashedChar = generatedHash.toCharArray();
        byte[] hashedBin = decode(hashedChar);
        String hashedBinString = new String(hashedBin);
        
        String hashedHex = javax.xml.bind.DatatypeConverter.printHexBinary(
                hashedBin).toLowerCase();
        
        // Print the results
        System.err.println("Generated hash (bin):\t\t" + hashedBinString);
        System.err.println("Generated hash (base16):\t" + hashedHex);
        System.err.println("Generated hash (base64):\t" + generatedHash);
        System.err.println("Generated hash String:\t\t" + hashed);
        System.err.println("Total runtime:\t\t\t" + (endTime - startTime) + "ms.");        
        
        // Print the Hash to out
        System.out.println(hashedHex);        
        
        // Do a number of tests on the hash to determine if it is a valid scrypt hash
        int params = Integer.valueOf(parts[2], 16);       
        
        if (parts.length != 5) {
            System.err.println("Failed: the generated hash does not consist of exact 5 parts.");
        }
        if (!parts[1].equals("s0")) {
            System.err.println("Failed: the generated hash does not start with s0.");
        }
        if (decode(parts[3].toCharArray()).length != 16) {
            System.err.println("Failed: the second part of the generated hash has an invalid length (" + 
                    decode(parts[3].toCharArray()).length + " instead of " + "16).");
        }
        if (decode(parts[4].toCharArray()).length != 32) {
            System.err.println("Failed: the third part of the generated hash has an invalid length" + 
                    decode(parts[4].toCharArray()).length + " instead of " + "32).");
        }
        if ((int)Math.pow(2, params >> 16 & 0xffff) != N) {
            System.err.println("Failed: the value of N is invalid.");
        }
        if ((params >> 8 & 0xff) != r) {
            System.err.println("Failed: the value of r is invalid.");
        }
        if ((params >> 0 & 0xff) != p) {
            System.err.println("Failed: the value of p is invalid.");
        }
    }
    
    public static String scrypt(String saltString, String passwd, int N, int r, int p) {
        try {
            char[] saltChar = saltString.toCharArray();
            byte[] salt = decode(saltChar);
            byte[] derived = SCrypt.scrypt(passwd.getBytes("UTF-8"), salt, N, r, p, 32);

            String params = Long.toString(log2(N) << 16L | r << 8 | p, 16);

            StringBuilder sb = new StringBuilder((salt.length + derived.length) * 2);
            sb.append("$s0$").append(params).append('$');
            sb.append(encode(salt)).append('$');
            sb.append(encode(derived));

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("JVM doesn't support UTF-8?");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("JVM doesn't support SHA1PRNG or HMAC_SHA256?");
        }
    }
    
    private static int log2(int n) {
        int log = 0;
        if ((n & 0xffff0000 ) != 0) { n >>>= 16; log = 16; }
        if (n >= 256) { n >>>= 8; log += 8; }
        if (n >= 16 ) { n >>>= 4; log += 4; }
        if (n >= 4  ) { n >>>= 2; log += 2; }
        return log + (n >>> 1);
    }
}
