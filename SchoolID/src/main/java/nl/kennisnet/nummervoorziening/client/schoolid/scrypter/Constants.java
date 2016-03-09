package nl.kennisnet.nummervoorziening.client.schoolid.scrypter;

/**
 * Constants for hashing pgn values. Should not be changed if you want always
 * get the same values after hashing.
 */
public interface Constants {

    String SALT = "rktYml0MIp9TC9u6Ny6uqw==";

    int N = 16384;

    int r = 8;

    int p = 1;
}
