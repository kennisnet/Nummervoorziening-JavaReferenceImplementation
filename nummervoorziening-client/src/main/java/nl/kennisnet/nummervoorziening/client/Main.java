package nl.kennisnet.nummervoorziening.client;

import school.id.eck.schemas.v1_0.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Main class. Contains program entry point.
 */
public class Main {

    private static final String WEB_SERVICE_APPLICATION_VERSION = "0.1.0-SNAPSHOT";

    /**
     * The main entry point for the program. This function demonstrates work
     * with Web Services via created util.
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
        List<Chain> activeChains = retrieveChainsResponse.getChain();
        System.out.println("Count of active chains:    " + activeChains.size());
        RetrieveSectorsResponse retrieveSectorsResponse = schoolIdServiceUtil.retrieveSectors();
        List<Sector> activeSectors = retrieveSectorsResponse.getSector();
        System.out.println("Count of active sectors:   " + activeSectors.size());
        System.out.println("Retrieving EckId for first active sector and first active chain:");
        String chainId = activeChains.get(0).getId();
        System.out.println("ChainId:    " + chainId);
        String sectorId = activeSectors.get(0).getId();
        System.out.println("SectorId:   " + sectorId);
        String hPgn = "hpgn";
        System.out.println("HPgn:       " + hPgn);
        RetrieveEckIdResponse retrieveEckIdResponse = schoolIdServiceUtil.retrieveEckId(chainId, sectorId, hPgn);
        System.out.println("Retrieved EckID: " + retrieveEckIdResponse.getEckId().getValue());
    }
}
