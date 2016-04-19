package nl.kennisnet.nummervoorziening.client.schoolid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads the configuration from the config.properties file
 */
public class Configuration {

    private static final String PROPERTIES_FILE_NAME = "config.properties";

    private static final String PROPERTIES_ENDPOINT_ADDRESS = "endpoint.address";

    private static final String PROPERTIES_CLIENT_INSTANCEOIN = "client.instanceOin";

    private static final String PROPERTIES_CERTIFICATE_KEYSTOREPATH = "certificate.KeyStorePath";

    private static final String PROPERTIES_CERTIFICATE_KEYSTORE_PASSWORD = "certificate.KeyStorePassword";

    private static final String PROPERTIES_CERTIFICATE_PASSWORD = "certificate.Password";


    //private static final String

    private Properties properties = new Properties();


    public Configuration() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file " + PROPERTIES_FILE_NAME + " not found in classpath");
        }

        inputStream.close();
    }

    public String getEndpointAddress() {
        return properties.getProperty(PROPERTIES_ENDPOINT_ADDRESS);
    }

    public String getClientInstanceOin() {
        return properties.getProperty(PROPERTIES_CLIENT_INSTANCEOIN);
    }

    public String getCertificateKeystorePath() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_KEYSTOREPATH);
    }

    public String getCertificateKeystorePassword() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_KEYSTORE_PASSWORD);
    }

    public String getCertificatePassword() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_PASSWORD);
    }



}
