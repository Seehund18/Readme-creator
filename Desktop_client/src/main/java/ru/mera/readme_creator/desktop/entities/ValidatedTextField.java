/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.desktop.entities;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ru.mera.readme_creator.desktop.interfaces.Validator;

/**
 * Entity which represents text field with validation status text and validation flag
 */
public class ValidatedTextField {
    private TextField textField;
    private Text statusText;
    private Boolean valid = false;

    public ValidatedTextField(TextField textField, Text statusText, Validator validator) {
        this.textField = textField;
        this.statusText = statusText;
        textField.textProperty().addListener(new StatusListener(this, validator));
    }

    public ValidatedTextField(Validator validator) {
        this(new TextField(), new Text(), validator);
    }

    public ValidatedTextField() {
        this(new Validator.DefaultValidator());
    }

    public TextField getTextField() {
        return textField;
    }
    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public Text getStatusText() {
        return statusText;
    }
    public void setStatusText(Text statusText) {
        this.statusText = statusText;
    }

    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}