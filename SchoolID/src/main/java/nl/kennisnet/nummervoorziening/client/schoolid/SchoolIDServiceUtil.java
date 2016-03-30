package nl.kennisnet.nummervoorziening.client.schoolid;

import school.id.eck.schemas.v1_0.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.datatype.XMLGregorianCalendar;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class that helps to work with Web Service.
 */
public class SchoolIDServiceUtil {

    private final SchoolID schoolID;

    /**
     * Initializes class for working with SchoolID Web Service.
     */
    public SchoolIDServiceUtil() throws NoSuchAlgorithmException, KeyManagementException {
        SchoolIDService schoolIDService = new SchoolIDService();
        schoolID = schoolIDService.getSchoolIDSoap10();
        disableSsl();
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
     * Invokes the School ID service to generate a School ID based on the
     * hashed PGN, Chain ID and Sector ID.
     *
     * @param hpgn       The scrypt hashed PGN.
     * @param chainGuid  A valid chain id.
     * @param sectorGuid A valid sector id.
     * @return If no validation or operational errors, a School ID.
     */
    public String generateSchoolID(String hpgn, String chainGuid, String sectorGuid) {
        RetrieveEckIdRequest retrieveEckIdRequest = new RetrieveEckIdRequest();
        retrieveEckIdRequest.setChainId(chainGuid);
        retrieveEckIdRequest.setSectorId(sectorGuid);
        HPgn hpgnWrapper = new HPgn();
        hpgnWrapper.setValue(hpgn);
        retrieveEckIdRequest.setHpgn(hpgnWrapper);
        return schoolID.retrieveEckId(retrieveEckIdRequest).getEckId().getValue();
    }

    /**
     * Executes Substitution operation for given parameters and returns the School ID.
     *
     * @param newHpgnValue  The scrypt hashed new PGN.
     * @param oldHpgnValue  The scrypt hashed old PGN.
     * @param chainGuid     A valid chain id.
     * @param sectorGuid    A valid sector id.
     * @param effectiveDate The date for the substitution to become active (optional).
     * @return The generated School ID.
     */
    public String replaceEckId(String newHpgnValue, String oldHpgnValue, String chainGuid, String sectorGuid,
                               XMLGregorianCalendar effectiveDate) {
        ReplaceEckIdRequest replaceEckIdRequest = new ReplaceEckIdRequest();
        replaceEckIdRequest.setChainId(chainGuid);
        replaceEckIdRequest.setSectorId(sectorGuid);
        HPgn newHpgn = new HPgn();
        newHpgn.setValue(newHpgnValue);
        replaceEckIdRequest.setHpgnNew(newHpgn);
        HPgn oldHpgn = new HPgn();
        oldHpgn.setValue(oldHpgnValue);
        replaceEckIdRequest.setHpgnOld(oldHpgn);
        replaceEckIdRequest.setEffectiveDate(effectiveDate);
        return schoolID.replaceEckId(replaceEckIdRequest).getEckId().getValue();
    }

    /**
     * Invokes the School ID service to start generating a batch of School IDs.
     *
     * @param listedHpgnMap Map with hashed PGN values as values and their indexes as keys.
     * @param chainGuid     A valid chain id.
     * @param sectorGuid    A valid sector id.
     * @return If no validation or operational errors, identifier of created batch.
     */
    public String submitSchoolIdBatch(Map<Integer, String> listedHpgnMap, String chainGuid, String sectorGuid) {
        SubmitEckIdBatchRequest submitEckIdBatchRequest = new SubmitEckIdBatchRequest();
        submitEckIdBatchRequest.setChainId(chainGuid);
        submitEckIdBatchRequest.setSectorId(sectorGuid);
        for (Map.Entry<Integer, String> entry : listedHpgnMap.entrySet()) {
            ListedHpgn listedHpgn = new ListedHpgn();
            listedHpgn.setIndex(entry.getKey());
            HPgn hPgnWrapper = new HPgn();
            hPgnWrapper.setValue(entry.getValue());
            listedHpgn.setHPgn(hPgnWrapper);
            submitEckIdBatchRequest.getHpgnList().add(listedHpgn);
        }
        return schoolID.submitEckIdBatch(submitEckIdBatchRequest).getBatchIdentifier().getValue();
    }

    /**
     * Invokes the School ID service to get list of generated School IDs for specified batch.
     *
     * @return If no validation or operational errors, response with failed and processed School IDs.
     */
    public SchoolIDBatch retrieveSchoolIdBatch(String batchIdentifier) {
        RetrieveEckIdBatchRequest request = new RetrieveEckIdBatchRequest();
        BatchIdentifier batchIdentifierWrapper = new BatchIdentifier();
        batchIdentifierWrapper.setValue(batchIdentifier);
        request.setBatchIdentifier(batchIdentifierWrapper);
        RetrieveEckIdBatchResponse response = schoolID.retrieveEckIdBatch(request);
        SchoolIDBatch schoolIDBatch = new SchoolIDBatch();
        schoolIDBatch.setSuccess(response.getSuccess().stream().collect(Collectors.toMap(ListedEckIdSuccess::getIndex,
            listedEckIdSuccess -> listedEckIdSuccess.getEckId().getValue())));
        schoolIDBatch.setFailed(response.getFailed().stream().collect(Collectors.toMap(ListedEckIdFailure::getIndex,
            ListedEckIdFailure::getErrorMessage)));
        return schoolIDBatch;
    }

    /**
     * Disables ssl verifications. It is needed in development if you are using
     * self signed certificate, this method should not be used in production.
     */
    private void disableSsl() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}
