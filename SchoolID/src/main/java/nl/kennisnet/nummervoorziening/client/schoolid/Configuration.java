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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads the configuration from the config.properties file and makes their values accessible throughout the
 * SchoolID module.
 */
public class Configuration {

    private static final String PROPERTIES_ENDPOINT_ADDRESS = "endpoint.address";

    private static final String PROPERTIES_CLIENT_INSTANCEOIN = "client.instanceOin";

    private static final String PROPERTIES_CERTIFICATE_KEYSTOREPATH = "certificate.KeyStorePath";

    private static final String PROPERTIES_CERTIFICATE_KEYSTORE_PASSWORD = "certificate.KeyStorePassword";

    private static final String PROPERTIES_CERTIFICATE_PASSWORD = "certificate.Password";

    private Properties properties = new Properties();

    /**
     * Instantiate the object and tries to load the config.properties file.
     *
     * @throws IOException Is thrown is the config.properties file could not be read.
     */
    public Configuration(String configFilePath) throws IOException {
        InputStream inputStream = new FileInputStream(configFilePath);

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file " + configFilePath + " not found");
        }

        inputStream.close();
    }

    /**
     * Gets the configured Endpoint URL Address as provided in the config.properties file.
     * @return The Endpoint URL Address
     */
    public String getEndpointAddress() {
        return properties.getProperty(PROPERTIES_ENDPOINT_ADDRESS);
    }

    /**
     * Gets the configured client Instance OIN as provided in the config.properties file. This value will be used
     * in the SOAP From Header to identify the client.
     *
     * @return The client Instance OIN
     */
    public String getClientInstanceOin() {
        return properties.getProperty(PROPERTIES_CLIENT_INSTANCEOIN);
    }

    /**
     * Gets the configured path to the Certificate Keystore  as provided in the config.properties file.
     * @return The path to the Certificate Keystore. Can be relative to the classpath, or an absolute value on the
     * filesystem.
     */
    public String getCertificateKeystorePath() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_KEYSTOREPATH);
    }

    /**
     * Gets the configured password of the Keystore as provided in the config.properties file.
     * @return The password for the Keystore
     */
    public String getCertificateKeystorePassword() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_KEYSTORE_PASSWORD);
    }

    /**
     * Gets the configured password of the Certificate as provided in the config.properties file.
     * @return The password of the Certificate
     */
    public String getCertificatePassword() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_PASSWORD);
    }



}
