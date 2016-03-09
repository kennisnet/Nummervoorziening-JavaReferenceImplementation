package nl.kennisnet.nummervoorziening.client.schoolid;

import school.id.eck.schemas.v1_0.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
     * Executes request to Web Service for getting information about its availability/configuration.
     *
     * @return PingResponse instance with information about Web Service.
     */
    public PingResponse ping() {
        return schoolID.ping(new PingRequest());
    }

    /**
     * Executes request to Web Service and returns response containing all active chains.
     *
     * @return RetrieveChainsResponse instance with list of all active chains.
     */
    public RetrieveChainsResponse retrieveChains() {
        return schoolID.retrieveChains(new RetrieveChainsRequest());
    }

    /**
     * Executes request to Web Service and returns response containing all active sectors.
     *
     * @return RetrieveSectorsResponse instance with list of all active sectors.
     */
    public RetrieveSectorsResponse retrieveSectors() {
        return schoolID.retrieveSectors(new RetrieveSectorsRequest());
    }

    /**
     * Executes request to Web Service and returns response containing generated EckId.
     *
     * @return RetrieveEckIdResponse instance with generated EckId.
     */
    public RetrieveEckIdResponse retrieveEckId(String chainId, String sectorId, String hPgnValue) {
        RetrieveEckIdRequest retrieveEckIdRequest = new RetrieveEckIdRequest();
        retrieveEckIdRequest.setChainId(chainId);
        retrieveEckIdRequest.setSectorId(sectorId);
        HPgn hPgn = new HPgn();
        hPgn.setValue(hPgnValue);
        retrieveEckIdRequest.setHpgn(hPgn);
        return schoolID.retrieveEckId(retrieveEckIdRequest);
    }

    /**
     * Disables ssl verifications. It is needed in development if you are using
     * self signed certificate, this method should not be used in production.
     */
    private void disableSsl() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}
