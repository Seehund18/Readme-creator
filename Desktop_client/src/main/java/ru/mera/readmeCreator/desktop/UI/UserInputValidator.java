/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.UI;

import ru.mera.readmeCreator.desktop.interfaces.Validator;

/**
 * Validator for userInput field
 */
class UserInputValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        //Value shouldn't be empty
        return value != null && !value.equals("");
    }
}