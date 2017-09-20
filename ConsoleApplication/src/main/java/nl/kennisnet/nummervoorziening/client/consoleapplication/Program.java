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
package nl.kennisnet.nummervoorziening.client.consoleapplication;

import nl.kennisnet.nummervoorziening.client.schoolid.SchoolIDBatch;
import nl.kennisnet.nummervoorziening.client.schoolid.SchoolIDServiceUtil;
import nl.kennisnet.nummervoorziening.client.schoolid.scrypter.ScryptUtil;
import school.id.eck.schemas.v1_0.Chain;
import school.id.eck.schemas.v1_0.Sector;

import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class. Contains program entry point.
 */
public class Program {

    private static final String WEB_SERVICE_APPLICATION_VERSION = "1.0.3-SNAPSHOT";

    private static final int BATCH_RETRIEVE_ATTEMPTS_COUNT = 10;

    private static final long RETRIEVE_SCHOOL_ID_BATCH_TIMEOUT = 25_000;

    private static SchoolIDServiceUtil schoolIDServiceUtil;

    /**
     * This class should not be instantiated.
     */
    private Program() {
    }

    /**
     * The main entry point for the program. This function demonstrates how to work with Web Services via the
     * SchoolID project.
     *
     * @param args Command line arguments to the program. Not Used.
     */
    public static void main(String[] args) throws GeneralSecurityException, InterruptedException, IOException {
        System.out.println("Current server information:");
        schoolIDServiceUtil = new SchoolIDServiceUtil();

        // Check if the Service is available
        if (!schoolIDServiceUtil.isNummervoorzieningServiceAvailable()) {
            System.out.println("Nummervoorziening service is not available, quitting.");
        } else {
            // Print some information about the service
            String applicationVersion = schoolIDServiceUtil.getApplicationVersion();
            System.out.println("Application version:\t\t" + applicationVersion);
            if (!WEB_SERVICE_APPLICATION_VERSION.equals(applicationVersion)) {
                System.out.println("Web Service Application version is different from intended (" +
                    WEB_SERVICE_APPLICATION_VERSION + " vs " + applicationVersion + ").");
            }
            System.out.println("System time:\t\t\t\t" + schoolIDServiceUtil.getSystemTime());
            System.out.println("Available:\t\t\t\t\t" + schoolIDServiceUtil.isNummervoorzieningServiceAvailable());

            // List number of active chains and sectors
            List<Chain> activeChains = schoolIDServiceUtil.getChains();
            System.out.println("Count of active chains:\t\t" + activeChains.size());

            List<Sector> activeSectors = schoolIDServiceUtil.getSectors();
            System.out.println("Count of active sectors:\t" + activeSectors.size());

            // Retrieve a Stampseudonym
            System.out.println("\nRetrieving Stampseudonym:");
            String studentStampseudonym = executeCreateStampseudonymTest("063138219");
            System.out.println("Retrieved Stampseudonym:\t" + studentStampseudonym + "\n");
            String teacherStampseudonym = executeCreateStampseudonymTest("20DP teacher@school.com");
            System.out.println("Retrieved Stampseudonym:\t" + teacherStampseudonym + "\n");

            // Execute a batch operation for retrieving Stampseudonyms
            Map<Integer, String> listedHpgnMap = new HashMap<>();
            listedHpgnMap.put(0, ScryptUtil.generateHexHash("063138219"));
            listedHpgnMap.put(1, ScryptUtil.generateHexHash("20DP teacher@school.com"));

            System.out.println("\nSubmitting Stampseudonym batch (with the same input):");
            executeStampseudonymBatchOperation(listedHpgnMap);

            // Retrieve a SchoolID
            System.out.println("\nRetrieving SchoolID for first active sector and first active chain:");
            String chainGuid = activeChains.get(0).getId();
            System.out.println("Chain Guid:\t\t\t\t\t" + chainGuid);
            String sectorGuid = activeSectors.get(0).getId();
            System.out.println("Sector Guid:\t\t\t\t" + sectorGuid);

            // Execute a number of valid tests
            System.out.println("Retrieved Student SchoolID:\t" +
                executeCreateEckIdTest(studentStampseudonym, chainGuid, sectorGuid));
            System.out.println("Retrieved Teacher SchoolID:\t" +
                executeCreateEckIdTest(teacherStampseudonym, chainGuid, sectorGuid));

            // Execute a batch operation
            Map<Integer, String> listedStampseudonymMap = new HashMap<>();
            listedStampseudonymMap.put(0, studentStampseudonym);
            listedStampseudonymMap.put(1, teacherStampseudonym);

            System.out.println("\nSubmitting SchoolID batch (with the same input):");
            executeEckIdBatchOperation(chainGuid, sectorGuid, listedStampseudonymMap);
        }
    }

    /**
     * Executes tests for retrieving Stampseudonym based on PGN
     * @param pgn The PGN to be hashed and send to Nummervoorziening to create a Stampseudonym.
     */
    private static String executeCreateStampseudonymTest(String pgn) {
        System.out.println("Pgn:\t\t\t\t\t\t" + pgn);

        // Generate scrypt hash of the given PGN
        String hpgn = ScryptUtil.generateHexHash(pgn);
        System.out.println("HPgn:\t\t\t\t\t\t" + hpgn);

        // Retrieve Stampseudonym from Nummervoorziening service
        return schoolIDServiceUtil.generateStampseudonym(hpgn);
    }

    /**
     * Executes test cases.
     *
     * @param stampseudonym     The Stampseudonym input.
     * @param chainGuid         A valid Chain Guid.
     * @param sectorGuid        A valid Sector Guid.
     */
    private static String executeCreateEckIdTest(String stampseudonym, String chainGuid, String sectorGuid) {
        // Retrieve SchoolID from Nummervoorziening service
        String eckId = schoolIDServiceUtil.generateSchoolID(stampseudonym, chainGuid, sectorGuid);
        return eckId;
    }

    /**
     * Executes batch operation.
     *
     * @param chainGuid                 A valid Chain Guid.
     * @param sectorGuid                A valid Sector Guid.
     * @param listedStampseudonymMap    Map with Stampseudonym values as values and their indexes as keys.
     */
    private static void executeEckIdBatchOperation(
        String chainGuid, String sectorGuid, Map<Integer, String> listedStampseudonymMap) throws InterruptedException {

        String batchIdentifier;

        try {
            batchIdentifier = schoolIDServiceUtil.submitEckIdBatch(listedStampseudonymMap, chainGuid, sectorGuid);
        } catch (SOAPFaultException e) {
            System.out.println("Exception thrown by service while trying to submit batch: " +
                e.getFault().getFaultActor());

            switch (e.getFault().getFaultActor()) {
                case "LimitDailyBatchSubmissionsExceededException":
                    System.out.println("Reached the limit of the amount of allowed daily submissions");
                    break;
                default:
                    System.out.println("No additional information available");
            }

            return;
        }

        System.out.println("Batch identifier:\t\t\t" + batchIdentifier);
        System.out.println("Waiting for processing...");

        SchoolIDBatch schoolIDBatch = waitForProcessing(batchIdentifier);

        if (null != schoolIDBatch) {
            System.out.println("Generated School IDs:\t\t" + schoolIDBatch.getSuccess());
            System.out.println("Failed School IDs:\t\t\t" + schoolIDBatch.getFailed());
        } else {
            System.out.println("Error occured: SchoolIDBatch is null.");
        }
    }

    /**
     * Executes batch operation.
     *
     * @param listedHpgnMap Map with hashed PGN values as values and their indexes as keys.
     */
    private static void executeStampseudonymBatchOperation(Map<Integer, String> listedHpgnMap) throws InterruptedException {
        String batchIdentifier;

        try {
            batchIdentifier = schoolIDServiceUtil.submitStampseudonymBatch(listedHpgnMap);
        } catch (SOAPFaultException e) {
            System.out.println("Exception thrown by service while trying to submit batch: " +
                e.getFault().getFaultActor());

            switch (e.getFault().getFaultActor()) {
                case "LimitDailyBatchSubmissionsExceededException":
                    System.out.println("Reached the limit of the amount of allowed daily submissions");
                    break;
                default:
                    System.out.println("No additional information available");
            }

            return;
        }

        System.out.println("Batch identifier:\t\t\t" + batchIdentifier);
        System.out.println("Waiting for processing...");

        SchoolIDBatch schoolIDBatch = waitForProcessing(batchIdentifier);

        if (null != schoolIDBatch) {
            System.out.println("Generated Stampseudonyms:\t" + schoolIDBatch.getSuccess());
            System.out.println("Failed Stampseudonyms:\t\t" + schoolIDBatch.getFailed());
        } else {
            System.out.println("Error occured: StampseudonymBatch is null.");
        }
    }

    private static SchoolIDBatch waitForProcessing(String batchIdentifier) throws InterruptedException {
        SchoolIDBatch schoolIDBatch = null;
        for (int i = 0; i < BATCH_RETRIEVE_ATTEMPTS_COUNT; i++) {
            Thread.sleep(RETRIEVE_SCHOOL_ID_BATCH_TIMEOUT);
            try {
                schoolIDBatch = schoolIDServiceUtil.retrieveSchoolIdBatch(batchIdentifier);
                break;
            } catch (SOAPFaultException e) {
                System.out.println("Exception thrown by service while trying to retrieve batch: " +
                    e.getFault().getFaultActor());

                switch (e.getFault().getFaultActor()) {
                    case "NotFinishedException":
                        System.out.println("Batch processing not finished yet...");
                        break;
                    case "TemporaryBlockedException":
                    case "ContentAlreadyRetrievedException":
                    case "ContentRemovedException":
                    default:
                        System.out.println("No additional information available");
                }
            }
        }
        return schoolIDBatch;
    }
}
