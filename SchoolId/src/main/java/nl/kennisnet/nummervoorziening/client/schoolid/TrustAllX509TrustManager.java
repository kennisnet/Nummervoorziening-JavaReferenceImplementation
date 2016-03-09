package nl.kennisnet.nummervoorziening.client.schoolid;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Provides an implementation of the <code>X509TrustManager</code> which does
 * not validate the supplied certificate. Do not use this class in production!
 */
public class TrustAllX509TrustManager implements X509TrustManager {

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        // do nothing
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        // do nothing
    }
}
