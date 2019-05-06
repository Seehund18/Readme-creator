/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Class through which properties of the application are controlled.
 * To use properties, class should be firstly initialized in the program.
 * For this, method init() is used, which must be called only once.
 */
public class PropertiesManager {
    private static final Logger log = LoggerFactory.getLogger(PropertiesManager.class);

    private static final File propertiesFile = new File("conf/config.properties");
    private static Properties properties;

    /**
     * Initialize PropertyManager. Must be called first
     * @throws PropertiesManagerException problems with config file
     */
    public static void init() throws PropertiesManagerException {
        //If init() already been called, do nothing
        if (properties != null) {
            log.debug("PropertiesManager already been initialized");
            return;
        }

        //If file doesn't exist, creating it
        if (!propertiesFile.exists()) {
            try {
                createPropertyFile();
            } catch (IOException e) {
                throw new PropertiesManagerException("Exception during first writing to property file", e);
            }
        }

        properties = new Properties();
        try {
            //Trying to load properties from file
            properties.load(new FileReader(propertiesFile));
        } catch (FileNotFoundException ex) {
            //File was not found (it may be deleted or moved by user)
            throw new PropertiesManagerException("No config file was found", ex);
        } catch (IOException ex) {
            throw new PropertiesManagerException("Exception while reading config file", ex);
        }
    }

    /**
     * Creates property file with needed properties
     */
    private static void createPropertyFile() throws IOException {
        new File("conf").mkdir();
        try (FileWriter out = new FileWriter(propertiesFile)) {
            out.write("webServiceURL");
        }
    }

    /**
     * Gets property value
     * @param key property which value is needed to be returned
     * @return value of the property or null if property is missing
     * @throws UnsupportedOperationException PropertyManager wasn't initialized
     */
    public static String getPropertyValue(String key) {
        if (properties == null) {
            throw new UnsupportedOperationException("PropertiesManager isn't initialized."
                    + " Method init() must be called first");
        }
        return properties.getProperty(key);
    }

    /**
     * Sets property value and saves it in the file
     * @param key property
     * @param value new value of the property
     * @return true - new property value is successfully saved;
     *         false - new property value is equals to the old one
     * @throws UnsupportedOperationException PropertyManager wasn't initialized first
     * @throws PropertiesManagerException Exception occurred during saving the file
     */
    public static boolean setPropertyValue(String key, String value) throws PropertiesManagerException {
        if (properties == null) {
            throw new UnsupportedOperationException("PropertiesManager isn't initialized."
                    + " Method init() must be called first");
        }
        if (properties.getProperty(key).equals(value)) {
            return false;
        }

        properties.setProperty(key, value);
        saveToFile();
        log.info("Value of property {} was set to {}", key, value);
        return true;
    }

    /**
     * Saves properties to file
     * @throws PropertiesManagerException error while writing properties to file
     */
    private static void saveToFile() throws PropertiesManagerException {
        try (FileWriter out = new FileWriter(propertiesFile)) {
            properties.store(out,"List of desktop client properties");
        } catch (IOException ex) {
            throw new PropertiesManagerException("Error while writing properties to file", ex);
        }
    }
}