/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class PropertiesManager {
    private static File propertiesFile = new File("src/main/resources/config.properties");
    private static Properties prop;
    private static final Logger log = LoggerFactory.getLogger(PropertiesManager.class);

    //Initialize PropertiesManager
    public static void init() throws PropertiesManagerException {
        //If init() already been called, do nothing
        if (prop != null) {
            log.info("PropertiesManager already been initialized");
            return;
        }

        prop = new Properties();
        try {
            //Trying to load properties from file
            prop.load(new FileReader(propertiesFile));
        } catch (FileNotFoundException ex) {
            //If file was not found (it may be deleted or moved by user)
            throw new PropertiesManagerException("No config file was found", ex);
        } catch (IOException ex) {
            log.error("Exception while reading config file");
            throw new PropertiesManagerException("Exception while reading config file", ex);
        }
    }

    /**
     * Gets property
     *
     * @param property
     * @return
     */
    public static String getPropertyValue(String property) {
        return prop.getProperty(property);
    }

    public static boolean setPropertyValue(String key, String value) throws PropertiesManagerException {
        if (getPropertyValue(key).equals(value)) {
            return false;
        }
        prop.setProperty(key, value);
        saveToFile();
        log.info("Value of property {} was set to {}", key, value);
        return true;
    }

    private static void saveToFile() throws PropertiesManagerException {
        try (FileWriter out = new FileWriter(propertiesFile)) {
            prop.store(out,"List of desktop client properties");
        } catch (IOException e) {
            log.error("Error while writing properties to file");
            throw new PropertiesManagerException("Error while writing properties to file");
        }
    }
}