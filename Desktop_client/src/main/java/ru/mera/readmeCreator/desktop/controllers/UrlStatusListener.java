/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Listener for webServiceUrl field
 */
class UrlStatusListener implements ChangeListener<String> {

    /**
     * Shows if is url in webServiceUrl field valid
     */
    static boolean isUrlValid;

    /**
     * Text which will dynamically change showing validation status
     */
    private Text urlStatus;

    /**
     * Validator for the webServiceUrl field
     */
    private UrlFieldValidator urlFieldValidator = new UrlFieldValidator();

    UrlStatusListener (Text urlStatus) {
        this.urlStatus = urlStatus;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (urlFieldValidator.isValid(newValue)) {
            //If validation was passed, "Valid URL" will be shown near the webServiceUrl field
            isUrlValid = true;
            urlStatus.setText("Valid URL");
            urlStatus.setFill(Color.GREEN);
            return;
        }
        //If not, "Not valid URL" will be shown
        isUrlValid = false;
        urlStatus.setText("Not valid URL");
        urlStatus.setFill(Color.RED);
    }
}