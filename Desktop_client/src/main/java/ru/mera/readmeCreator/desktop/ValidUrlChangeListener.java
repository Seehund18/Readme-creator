/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.apache.commons.validator.routines.UrlValidator;

public abstract class ValidUrlChangeListener implements ChangeListener<String> {

    @Override
    public abstract void changed(ObservableValue<? extends String> observable, String oldValue, String newValue);

    boolean isValid(String url) {
        //Checking what url is only server url without path to any resource.
        //For that, using regex pattern which checks that between beginning and ending of the string are only 2 or 3 slashes '/'
        //For example: http://myService.ru is correct; http://myService.ru/files/HelloWorld.rtf is wrong
        if(!url.matches("^http://[^/]+$")) {
            return false;
        }

        //Using UrlValidator class from apache.commons library to check the rest
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        return urlValidator.isValid(url);
    }
}
