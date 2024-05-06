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
package nl.kennisnet.nummervoorziening.client.eckid.impl;

import nl.kennisnet.nummervoorziening.client.eckid.Configuration;

import java.io.*;
import java.util.Properties;

/**
 * Reads the configuration from the config.properties file and makes their values accessible throughout the
 * EckID module.
 */
public class ConfigurationImpl implements Configuration {

    private static final String PROPERTIES_FILE_NAME = "config.properties";

    private static final String PARENT_DIRECTORY_NAME = "JavaReferenceImplementation";

    private final Properties properties = new Properties();

    /**
     * Instantiate the object and tries to load the config.properties file.
     *
     * @param  configFilePath The location to the config.properties file.
     * @throws IOException Is thrown is the config.properties file could not be read.
     */
    public ConfigurationImpl(String configFilePath) throws IOException {
        // We assume that properties file is placed in project root folder, but
        // execution of program can be started from any subfolder of project.
        String parentPath = configFilePath;
        if (parentPath.indexOf(PARENT_DIRECTORY_NAME) > 0) {
            parentPath = configFilePath.substring(0, configFilePath.indexOf(PARENT_DIRECTORY_NAME) +
                PARENT_DIRECTORY_NAME.length());
        }

        try (InputStream inputStream = new FileInputStream(parentPath + File.separator + PROPERTIES_FILE_NAME)) {
            properties.load(inputStream);
            // Update keystore path based on current path
            this.setCertificateKeyStorePath(parentPath + File.separator + this.getCertificateKeyStorePath());
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file " + configFilePath + " not found");
        }
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
     * Gets the configured path to the Certificate Keystore as provided in the config.properties file.
     * @return The path to the Certificate Keystore. Can be relative to the classpath, or an absolute value on the
     * filesystem.
     */
    public String getCertificateKeyStorePath() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_KEYSTOREPATH);
    }

    /**
     * Gets the configured password of the Keystore as provided in the config.properties file.
     * @return The password for the Keystore
     */
    public String getCertificateKeyStorePassword() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_KEYSTORE_PASSWORD);
    }

    /**
     * Gets the configured password of the Certificate as provided in the config.properties file.
     * @return The password of the Certificate
     */
    public String getCertificatePassword() {
        return properties.getProperty(PROPERTIES_CERTIFICATE_PASSWORD);
    }

    /**
     * Gets the configured first level salt.
     * @return the configured first level salt.
     */
    public String getFirstLevelSalt() {
        return properties.getProperty(PROPERTIES_FIRST_LEVEL_SALT);
    }

    /**
     * Sets the configured path to the Certificate Keystore as provided in the config.properties file.     *
     */
    private void setCertificateKeyStorePath(String keyStorePath) {
        properties.setProperty(PROPERTIES_CERTIFICATE_KEYSTOREPATH, keyStorePath);
    }

}
