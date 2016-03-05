package nl.kennisnet.nummervoorziening.client;

import school.id.eck.schemas.v1_0.PingResponse;
import school.id.eck.schemas.v1_0.RetrieveChainsResponse;
import school.id.eck.schemas.v1_0.RetrieveSectorsResponse;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Main class. Contains program entry point.
 */
public class Main {

    private static final String WEB_SERVICE_APPLICATION_VERSION = "0.1.0-SNAPSHOT";

    /**
     * The main entry point for the program.
     *
     * @param args Command line arguments to the program. Not Used.
     */
    public static void main(String args[]) throws NoSuchAlgorithmException, KeyManagementException {
        System.out.println("Current server information:");
        SchoolIdServiceUtil schoolIdServiceUtil = new SchoolIdServiceUtil();
        PingResponse pingResponse = schoolIdServiceUtil.ping();
        String applicationVersion = pingResponse.getApplicationVersion();
        System.out.println("Application version:       " + applicationVersion);
        if (!WEB_SERVICE_APPLICATION_VERSION.equals(applicationVersion)) {
            System.out.println("Web Service Application version is different from intended (" +
                    WEB_SERVICE_APPLICATION_VERSION + "), finishing.");
            return;
        }
        System.out.println("System time:               " + pingResponse.getSystemTime());
        System.out.println("Available:                 " + pingResponse.isAvailable());
        if (!pingResponse.isAvailable()) {
            System.out.println("Web Service is not available, finishing.");
            return;
        }
        RetrieveChainsResponse retrieveChainsResponse = schoolIdServiceUtil.retrieveChains();
        System.out.println("Count of active chains:    " + retrieveChainsResponse.getChain().size());
        RetrieveSectorsResponse retrieveSectorsResponse = schoolIdServiceUtil.retrieveSectors();
        System.out.println("Count of active sectors:   " + retrieveSectorsResponse.getSector().size());
    }
}
