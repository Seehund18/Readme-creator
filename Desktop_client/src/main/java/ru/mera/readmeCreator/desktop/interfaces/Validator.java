/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.interfaces;

/**
 * Interaface for validators
 */
public interface Validator {

    /**
     * Checks if value is valid
     * @param value value that need to be validated
     * @return true - value is valid;
     *         false - value isn't valid
     */
    boolean isValid(String value);
}
