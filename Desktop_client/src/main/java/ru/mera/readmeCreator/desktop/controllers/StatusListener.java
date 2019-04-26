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
import ru.mera.readmeCreator.desktop.entities.ValidatedTextField;
import ru.mera.readmeCreator.desktop.interfaces.Validator;

/**
 * Listener for fields
 */
public class StatusListener implements ChangeListener<String> {

    private ValidatedTextField field;

    /**
     * Validator for the field
     */
    private Validator fieldValidator;

    public StatusListener(ValidatedTextField field, Validator validator) {
        this.field = field;
        this.fieldValidator = validator;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (fieldValidator.isValid(newValue)) {
            //If validation was passed, "Valid URL" will be shown near the webServiceUrl field
            field.setValid(true);
            field.getStatusText().setText("Valid");
            field.getStatusText().setFill(Color.GREEN);
            return;
        }
        //If not, "Not valid URL" will be shown
        field.setValid(false);
        field.getStatusText().setText("Not valid");
        field.getStatusText().setFill(Color.RED);
    }
}