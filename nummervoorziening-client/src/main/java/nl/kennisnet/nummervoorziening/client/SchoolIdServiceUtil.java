package nl.kennisnet.nummervoorziening.client;

import school.id.eck.schemas.v1_0.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class that helps to work with Web Service.
 */
public class SchoolIdServiceUtil {

    private final SchoolID schoolID;

    /**
     * Initializes class for working with SchoolID Web Service.
     */
    public SchoolIdServiceUtil() throws NoSuchAlgorithmException, KeyManagementException {
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
     * @return RetrieveChainsResponse instance with list of active chains.
     */
    public RetrieveChainsResponse retrieveChains() {
        return schoolID.retrieveChains(new RetrieveChainsRequest());
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
