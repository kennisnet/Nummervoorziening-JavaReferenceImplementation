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
package nl.kennisnet.nummervoorziening.client.eckid;


/**
 * The configuration options which needs to be supplied to be able to use the EckIdService.
 */
public interface Configuration {

    String PROPERTIES_ENDPOINT_ADDRESS = "endpoint.address";

    String PROPERTIES_CLIENT_INSTANCEOIN = "client.instanceOin";

    String PROPERTIES_CERTIFICATE_KEYSTOREPATH = "certificate.KeyStorePath";

    String PROPERTIES_CERTIFICATE_KEYSTORE_PASSWORD = "certificate.KeyStorePassword";

    String PROPERTIES_CERTIFICATE_PASSWORD = "certificate.Password";

    /** Salt to be used as input for the client side first level hashing algorithm. */
    String PROPERTIES_FIRST_LEVEL_SALT = "first_level.salt";

    /**
     * Gets the configured Endpoint URL Address as provided in the config.properties file.
     * @return The Endpoint URL Address
     */
    String getEndpointAddress();

    /**
     * Gets the configured client Instance OIN as provided in the config.properties file. This value will be used
     * in the SOAP From Header to identify the client.
     *
     * @return The client Instance OIN
     */
    String getClientInstanceOin();

    /**
     * Gets the configured path to the Certificate Keystore as provided in the config.properties file.
     * @return The path to the Certificate Keystore. Can be relative to the classpath, or an absolute value on the
     * filesystem.
     */
    String getCertificateKeyStorePath();


    /**
     * Gets the configured password of the Keystore as provided in the config.properties file.
     * @return The password for the Keystore
     */
    String getCertificateKeyStorePassword();
    /**
     * Gets the configured password of the Certificate as provided in the config.properties file.
     * @return The password of the Certificate
     */
    String getCertificatePassword();

    /**
     * Gets the configured first level salt.
     * @return the configured first level salt.
     */
    String getFirstLevelSalt();

}
