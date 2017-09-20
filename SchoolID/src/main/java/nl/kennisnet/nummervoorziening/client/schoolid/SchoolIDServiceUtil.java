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
package nl.kennisnet.nummervoorziening.client.schoolid;

import school.id.eck.schemas.v1_0.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class that helps to work with Web Service.
 */
public class SchoolIDServiceUtil {

    private final SchoolID schoolID;

    private static final String PROPERTIES_FILE_NAME = "config.properties";

    private static final String PARENT_DIRECTORY_NAME = "JavaReferenceImplementation";

    private static Configuration configuration;

    /**
     * Initializes class for working with SchoolID Web Service.
     */
    public SchoolIDServiceUtil() throws GeneralSecurityException, IOException {
        SchoolIDService schoolIDService = new SchoolIDService();

        // Set the correct path to the config file (which is in the parent directory)
        String cwdPath = System.getProperty("user.dir");
        // We assume that properties file is placed in project root folder, but
        // execution of program can be started from any subfolder of project.
        String parentPath = cwdPath.substring(0, cwdPath.indexOf(PARENT_DIRECTORY_NAME) +
            PARENT_DIRECTORY_NAME.length());

        // Initialize the Configuration class
        configuration = new Configuration(parentPath + File.separator + PROPERTIES_FILE_NAME);

        // Explicitly enable WS-Addressing (required by the Nummervoorziening service)
        schoolID = schoolIDService.getSchoolIDSoap10(new javax.xml.ws.soap.AddressingFeature(true, true));

        BindingProvider bindingProvider = (BindingProvider) schoolID;
        Binding binding = bindingProvider.getBinding();

        // Override the default endpoint address
        bindingProvider.getRequestContext().put(bindingProvider.ENDPOINT_ADDRESS_PROPERTY,
            configuration.getEndpointAddress());

        // Add a Message Handler to be able to modify the Soap Headers
        List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(new AuthorizedSoapHeaderOinInterceptor());
        binding.setHandlerChain(handlerChain);

        configureSsl();
    }

    /**
     * Executes request to Web Service for getting information about its availability.
     *
     * @return true if service available, false - otherwise.
     */
    public boolean isNummervoorzieningServiceAvailable() {
        return schoolID.ping(new PingRequest()).isAvailable();
    }

    /**
     * Executes request to Web Service and returns current application version.
     *
     * @return current web service application version.
     */
    public String getApplicationVersion() {
        return schoolID.ping(new PingRequest()).getApplicationVersion();
    }

    /**
     * Executes request to Web Service and returns server's system time.
     *
     * @return server's system time.
     */
    public XMLGregorianCalendar getSystemTime() {
        return schoolID.ping(new PingRequest()).getSystemTime();
    }

    /**
     * Retrieves a list of currently available chains.
     *
     * @return list of all active chains.
     */
    public List<Chain> getChains() {
        return schoolID.retrieveChains(new RetrieveChainsRequest()).getChain();
    }

    /**
     * Retrieves a list of currently available sectors.
     *
     * @return list of all active sectors.
     */
    public List<Sector> getSectors() {
        return schoolID.retrieveSectors(new RetrieveSectorsRequest()).getSector();
    }

    /**
     * Invokes the School ID service to generate a Stampseudonym based on the hashed PGN.
     *
     * @param hpgn      The scrypt hashed PGN.
     * @return If no validation or operational errors, a Stampseudonym.
     */
    public String generateStampseudonym(String hpgn) {
        RetrieveStampseudonymRequest retrieveStampseudonymRequest = new RetrieveStampseudonymRequest();
        HPgn hpgnWrapper = new HPgn();
        hpgnWrapper.setValue(hpgn);
        retrieveStampseudonymRequest.setHpgn(hpgnWrapper);

        return schoolID.retrieveStampseudonym(retrieveStampseudonymRequest).getStampseudonym().getValue();
    }

    /**
     * Invokes the School ID service to generate a School ID based on the stampseudonym, Chain ID and Sector ID.
     *
     * @param stampseudonym       The stampseudonym.
     * @param chainGuid  A valid chain id.
     * @param sectorGuid A valid sector id.
     * @return If no validation or operational errors, a School ID.
     */
    public String generateSchoolID(String stampseudonym, String chainGuid, String sectorGuid) {
        RetrieveEckIdRequest retrieveEckIdRequest = new RetrieveEckIdRequest();
        retrieveEckIdRequest.setChainId(chainGuid);
        retrieveEckIdRequest.setSectorId(sectorGuid);

        Stampseudonym stampseudonymWrapper = new Stampseudonym();
        stampseudonymWrapper.setValue(stampseudonym);
        retrieveEckIdRequest.setStampseudonym(stampseudonymWrapper);

        return schoolID.retrieveEckId(retrieveEckIdRequest).getEckId().getValue();
    }

    /**
     * Executes Substitution operation for given parameters and returns new stampseudonym.
     *
     * @param newHpgnValue  The scrypt hashed new PGN.
     * @param oldHpgnValue  The scrypt hashed old PGN.
     * @param effectiveDate The date for the substitution to become active (optional).
     * @return The generated Stampseudonym.
     */
    public String replaceStampseudonym(String newHpgnValue, String oldHpgnValue, XMLGregorianCalendar effectiveDate) {
        ReplaceStampseudonymRequest replaceStampseudonymRequest = new ReplaceStampseudonymRequest();
        HPgn newHpgn = new HPgn();
        HPgn oldHpgn = new HPgn();

        newHpgn.setValue(newHpgnValue);
        oldHpgn.setValue(oldHpgnValue);
        replaceStampseudonymRequest.setHpgnNew(newHpgn);
        replaceStampseudonymRequest.setHpgnOld(oldHpgn);
        replaceStampseudonymRequest.setEffectiveDate(effectiveDate);

        return schoolID.replaceStampseudonym(replaceStampseudonymRequest).getStampseudonym().getValue();
    }

    /**
     * Invokes the School ID service to start generating a batch of School IDs based on passed input values.
     *
     * @param listedStampseudonymMap Map with Stampseudonym values as values and their indexes as keys.
     * @param chainGuid     A valid chain id.
     * @param sectorGuid    A valid sector id.
     * @return If no validation or operational errors, identifier of the created batch for retrieving the results.
     */
    public String submitEckIdBatch(
        Map<Integer, String> listedStampseudonymMap, String chainGuid, String sectorGuid) {

        SubmitEckIdBatchRequest submitEckIdBatchRequest = new SubmitEckIdBatchRequest();
        submitEckIdBatchRequest.setChainId(chainGuid);
        submitEckIdBatchRequest.setSectorId(sectorGuid);

        for (Map.Entry<Integer, String> entry : listedStampseudonymMap.entrySet()) {
            ListedStampseudonym listedStampseudonym = new ListedStampseudonym();
            listedStampseudonym.setIndex(entry.getKey());
            Stampseudonym stampseudonymWrapper = new Stampseudonym();
            stampseudonymWrapper.setValue(entry.getValue());
            listedStampseudonym.setStampseudonym(stampseudonymWrapper);
            submitEckIdBatchRequest.getStampseudonymList().add(listedStampseudonym);
        }

        return schoolID.submitEckIdBatch(submitEckIdBatchRequest).getBatchIdentifier().getValue();
    }

    /**
     * Invokes the School ID service to start generating a batch of stampseudonyms based on passed input values.
     *
     * @param listedHPgnMap Map with HPgn values as values and their indexes as keys.
     * @return If no validation or operational errors, identifier of the created batch for retrieving the results.
     */
    public String submitStampseudonymBatch(Map<Integer, String> listedHPgnMap) {

        SubmitStampseudonymBatchRequest submitStampseudonymBatchRequest = new SubmitStampseudonymBatchRequest();

        for (Map.Entry<Integer, String> entry : listedHPgnMap.entrySet()) {
            ListedHpgn listedHpgn = new ListedHpgn();
            listedHpgn.setIndex(entry.getKey());
            HPgn hpgnWrapper = new HPgn();
            hpgnWrapper.setValue(entry.getValue());
            listedHpgn.setHPgn(hpgnWrapper);
            submitStampseudonymBatchRequest.getHpgnList().add(listedHpgn);
        }

        return schoolID.submitStampseudonymBatch(submitStampseudonymBatchRequest).getBatchIdentifier().getValue();
    }

    /**
     * Invokes the School ID service to get list of generated School IDs for specified batch.
     *
     * @return If no validation or operational errors, response with failed and processed School IDs.
     */
    public SchoolIDBatch retrieveSchoolIdBatch(String batchIdentifier) {
        RetrieveBatchRequest request = new RetrieveBatchRequest();
        BatchIdentifier batchIdentifierWrapper = new BatchIdentifier();
        SchoolIDBatch schoolIDBatch = new SchoolIDBatch();

        batchIdentifierWrapper.setValue(batchIdentifier);
        request.setBatchIdentifier(batchIdentifierWrapper);
        RetrieveBatchResponse response = schoolID.retrieveBatch(request);

        schoolIDBatch.setSuccess(response.getSuccess().stream().collect(Collectors.toMap(ListedEntitySuccess::getIndex,
            listedEntitySuccess -> listedEntitySuccess.getValue())));
        schoolIDBatch.setFailed(response.getFailed().stream().collect(Collectors.toMap(ListedEntityFailure::getIndex,
            ListedEntityFailure::getErrorMessage)));

        return schoolIDBatch;
    }

    /**
     * Configures SSL/TLS communication and disables certificate validation. It is needed in development if you are
     * using self signed certificate, this method should not be used in production.
     * Configure the SSL configuration to be able to connect/use the webservices for test purpose only.
     */
    private void configureSsl() throws GeneralSecurityException {
        try {
            // Let's initialize our test keystore which is supplied within this project to simplify
            // running these tests on different machines. This should not be used in the actual implementation!!!

            final KeyStore keyStore = KeyStore.getInstance("JKS");
            InputStream is = SchoolIDServiceUtil.class.getResourceAsStream(
                configuration.getCertificateKeystorePath()
            );

            // Check if file exists in Class path. If not, retry using absolute path. If still not found, throw
            // an Exception
            if (is == null) {
                is = new FileInputStream(configuration.getCertificateKeystorePath());

                if (is == null) {
                    throw new IOException("File " + configuration.getCertificateKeystorePath() + " not found in " +
                        "classpath and/or filesystem");
                }
            }

            keyStore.load(is, configuration.getCertificateKeystorePassword().toCharArray());

            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, configuration.getCertificatePassword().toCharArray());

            // Instead of using the TrustAllX509TrustManager which will trust all certificates we should limit the
            // certificates which are accepted, to have at least some restriction.
            // final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // tmf.init(keyStore);

            // Creates the socket factory for HttpsURLConnection which will use our keystore
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the Instance OIN of the current application. Normally, this OIN will be based on the BRIN number of
     * the School which wants to use the Nummervoorziening application.
     *
     * @return The configured Instance OIN
     */
    public static String getInstanceOin() {
        return configuration.getClientInstanceOin();
    }
}
