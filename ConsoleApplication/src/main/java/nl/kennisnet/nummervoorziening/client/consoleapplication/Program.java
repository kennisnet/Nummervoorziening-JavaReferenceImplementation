package nl.kennisnet.nummervoorziening.client.consoleapplication;

import nl.kennisnet.nummervoorziening.client.schoolid.SchoolIDServiceUtil;
import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import school.id.eck.schemas.v1_0.Chain;
import school.id.eck.schemas.v1_0.Sector;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Main class. Contains program entry point.
 */
public class Program {

    private static final String WEB_SERVICE_APPLICATION_VERSION = "0.1.0-SNAPSHOT";
    private static SchoolIDServiceUtil schoolIDServiceUtil;

    /**
     * The main entry point for the program. This function demonstrates work
     * with Web Services via SchoolID project.
     *
     * @param args Command line arguments to the program. Not Used.
     */
    public static void main(String args[]) throws NoSuchAlgorithmException, KeyManagementException {
        System.out.println("Current server information:");
        schoolIDServiceUtil = new SchoolIDServiceUtil();

        if (!schoolIDServiceUtil.isNummervoorzieningServiceAvailable()) {
            System.out.println("Nummervoorziening service is not available, finishing.");
        } else {
            String applicationVersion = schoolIDServiceUtil.getApplicationVersion();
            System.out.println("Application version:\t\t" + applicationVersion);
            if (!WEB_SERVICE_APPLICATION_VERSION.equals(applicationVersion)) {
                System.out.println("Web Service Application version is different from intended (" +
                    WEB_SERVICE_APPLICATION_VERSION + "), finishing.");
                return;
            }
            System.out.println("System time:\t\t\t\t" + schoolIDServiceUtil.getSystemTime());
            System.out.println("Available:\t\t\t\t\t" + schoolIDServiceUtil.isNummervoorzieningServiceAvailable());

            List<Chain> activeChains = schoolIDServiceUtil.getChains();
            System.out.println("Count of active chains:\t\t" + activeChains.size());

            List<Sector> activeSectors = schoolIDServiceUtil.getSectors();
            System.out.println("Count of active sectors:\t" + activeSectors.size());

            System.out.println("\nRetrieving SchoolID for first active sector and first active chain:");
            String chainId = activeChains.get(0).getId();
            System.out.println("ChainId:\t\t\t\t\t" + chainId);
            String sectorId = activeSectors.get(0).getId();
            System.out.println("SectorId:\t\t\t\t\t" + sectorId);

            // Execute a number of valid tests
            executeClientTest("063138219", chainId, sectorId);
            executeClientTest("20DP teacher@school.com", chainId, sectorId);
        }
    }

    /**
     * Executes test cases
     * @param pgn The PGN input
     * @param chainGuid A valid Chain Guid
     * @param sectorGuid A valid Sector Guid
     */
    private static void executeClientTest(String pgn, String chainGuid, String sectorGuid) {
        System.out.println("Pgn:\t\t\t\t\t\t" + pgn);

        // Generate scrypt hash of the given PGN
        String hpgn = ScryptUtil.generateHexHash(pgn);
        System.out.println("HPgn:\t\t\t\t\t\t" + hpgn);

        // Retrieve SchoolID from Nummervoorziening service
        String eckId = schoolIDServiceUtil.generateSchoolID(hpgn, chainGuid, sectorGuid);
        System.out.println("Retrieved SchoolID:\t\t\t" + eckId);
        System.out.println();
    }
}
