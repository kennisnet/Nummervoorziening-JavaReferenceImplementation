package nl.kennisnet.nummervoorziening.client;

import school.id.eck.schemas.v1_0.PingResponse;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Main class. Contains program entry point.
 */
public class Main {

    /**
     * The main entry point for the program.
     *
     * @param args Command line arguments to the program. Not Used.
     */
    public static void main(String args[]) throws NoSuchAlgorithmException, KeyManagementException {
        System.out.println("Current server information:");
        SchoolIdServiceUtil schoolIdServiceUtil = new SchoolIdServiceUtil();
        PingResponse pingResponse = schoolIdServiceUtil.ping();
        System.out.println("Application version: " + pingResponse.getApplicationVersion());
        System.out.println("Available:           " + pingResponse.isAvailable());
        System.out.println("System time:         " + pingResponse.getSystemTime());
    }
}
