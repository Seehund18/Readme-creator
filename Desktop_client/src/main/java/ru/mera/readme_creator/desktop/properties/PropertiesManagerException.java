/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.desktop.properties;

/**
 * Represents all exceptions thrown from PropertiesManager class.
 * Exceptions from PropertiesManager are wrapped into this one.
 */
public class PropertiesManagerException extends Exception {

    PropertiesManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    PropertiesManagerException(String message) {
        super(message);
    }
}
