/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.UrlValidator;

public class UrlStatusListener implements ValidatedChangeListener {
    private Text statusField;
    private boolean statusFlag;

    UrlStatusListener(Text statusField, boolean statusFlag) {
        this.statusField = statusField;
        this.statusFlag = statusFlag;
    }

    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(isValid(newValue)) {
            statusField.setText("Valid URL");
            statusField.setFill(Color.GREEN);
            statusFlag = true;
            return;
        }
        statusFlag = false;
        statusField.setText("Not valid URL");
        statusField.setFill(Color.RED);
    }

    @Override
    public boolean isValid(String url) {
        //Checking that url is the server url without path to any resource.
        //For that, using regex pattern, which checks that between beginning and ending of the string are only 2 slashes '/'.
        //For example: http://myService.ru is correct (2 slashes); http://myService.ru/files/HelloWorld.rtf is wrong (4 slashes)
        if(!url.matches("^http://[^/]+$")) {
            return false;
        }

        //Using UrlValidator class from apache.commons library to check the rest
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        return urlValidator.isValid(url);
    }
}
