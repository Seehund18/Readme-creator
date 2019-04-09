/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

/**
 * Represents all exceptions thrown from PropertiesManager class.
 * Exceptions from PropertiesManager are wrapped into this one.
 * It extends RuntimeException because it must be unchecked exception to be thrown from static block of PropertiesManager.
 */
public class PropertiesManagerException extends Exception {

    public PropertiesManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertiesManagerException(String message) {
        super(message);
    }
}