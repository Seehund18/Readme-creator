/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.UI;

import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import org.apache.commons.validator.routines.UrlValidator;
import ru.mera.readmeCreator.desktop.ValidatedChangeListener;

import static ru.mera.readmeCreator.desktop.UI.UiElements.urlStatus;

/**
 * Listener for webServiceUrlField
 */
class UrlStatusListener implements ValidatedChangeListener {
    private static boolean isUrlValid;

    static boolean isUrlValid() {
        return isUrlValid;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(isValid(newValue)) {
            isUrlValid = true;
            urlStatus.setText("Valid URL");
            urlStatus.setFill(Color.GREEN);
            return;
        }
        isUrlValid = false;
        urlStatus.setText("Not valid URL");
        urlStatus.setFill(Color.RED);
    }

    /**
     * Checking that url is the server url without path to any resource.
     * For that, using regex pattern, which checks that there are
     * only two slashes '/' between the beginning and the ending of the string.
     * For example: http://myService.ru is correct (2 slashes);
     *              http://myService.ru/files/HelloWorld.rtf is wrong (4 slashes)
     * @param url
     * @return
     */
    @Override
    public boolean isValid(String url) {
        //Checking that url is the server url without path to any resource.
        //For that, using regex pattern, which checks that between beginning and ending of the string are only 2 slashes '/'.
        //For example: http://myService.ru is correct (2 slashes); http://myService.ru/files/HelloWorld.rtf is wrong (4 slashes)
        if(!url.matches("^http://[^/]+$")) {
            return false;
        }

        //Using UrlValidator class from apache.commons library to check the url
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        return urlValidator.isValid(url);
    }
}
