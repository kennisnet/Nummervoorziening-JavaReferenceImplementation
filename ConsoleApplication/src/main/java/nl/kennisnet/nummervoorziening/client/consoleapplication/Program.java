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

    /**
     * The main entry point for the program. This function demonstrates work
     * with Web Services via SchoolID project.
     *
     * @param args Command line arguments to the program. Not Used.
     */
    public static void main(String args[]) throws NoSuchAlgorithmException, KeyManagementException {
        System.out.println("Current server information:");
        SchoolIDServiceUtil schoolIdServiceUtil = new SchoolIDServiceUtil();
        String applicationVersion = schoolIdServiceUtil.getApplicationVersion();
        System.out.println("Application version:\t\t" + applicationVersion);
        if (!WEB_SERVICE_APPLICATION_VERSION.equals(applicationVersion)) {
            System.out.println("Web Service Application version is different from intended (" +
                    WEB_SERVICE_APPLICATION_VERSION + "), finishing.");
            return;
        }
        System.out.println("System time:\t\t\t\t" + schoolIdServiceUtil.getSystemTime());
        System.out.println("Available:\t\t\t\t\t" + schoolIdServiceUtil.isWebServiceAvailable());
        if (!schoolIdServiceUtil.isWebServiceAvailable()) {
            System.out.println("Web Service is not available, finishing.");
            return;
        }
        List<Chain> activeChains = schoolIdServiceUtil.getChains();
        System.out.println("Count of active chains:\t\t" + activeChains.size());
        List<Sector> activeSectors = schoolIdServiceUtil.getSectors();
        System.out.println("Count of active sectors:\t" + activeSectors.size());
        System.out.println("\nRetrieving EckId for first active sector and first active chain:");
        String chainId = activeChains.get(0).getId();
        System.out.println("ChainId:\t\t\t\t\t" + chainId);
        String sectorId = activeSectors.get(0).getId();
        System.out.println("SectorId:\t\t\t\t\t" + sectorId);
        String pgn = "pgn";
        System.out.println("Pgn:\t\t\t\t\t\t" + pgn);
        String hpgn = ScryptUtil.generateHexHash(pgn);
        System.out.println("HPgn:\t\t\t\t\t\t" + hpgn);
        String eckId = schoolIdServiceUtil.generateSchoolID(hpgn, chainId, sectorId);
        System.out.println("Retrieved EckID:\t\t\t" + eckId);
    }
}
