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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    private static Properties prop;
    private static final Logger log = LoggerFactory.getLogger(PropertiesManager.class);

    static {
        //Basic class initializer
        File propertiesFile = new File("src/main/resources/confi.properties");
        prop = new Properties();
        try {
            prop.load(new FileReader(propertiesFile));
        } catch (FileNotFoundException ex) {
            throw new PropertiesManagerException("No config file was found", ex);
        } catch (IOException ex) {
            throw new PropertiesManagerException("Exception while reading config file", ex);
        }
    }

    public static String getPropertyValue(String property) {
        return prop.getProperty(property);
    }
}