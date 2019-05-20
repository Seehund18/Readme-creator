/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.desktop.controllers;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.mera.readme_creator.desktop.entities.JiraPair;
import ru.mera.readme_creator.desktop.interfaces.AlertSender;

import java.util.Optional;

/**
 * Dialog, which is shown when user pressed "+" or "Edit" button,
 * where user write down jiraId and jiraDescription.
 * Difference between "Add" and "Edit" dialog type is that button and title differs
 */
public class JiraInputDialog implements AlertSender {

    /**
     * Skeleton of this dialog window
     */
    private Dialog<JiraPair> dialog = new Dialog<>();

    /**
     * Flag indicated whether jira ID is valid or not
     */
    private boolean idValid;

    /**
     * Type of dialog for this dialog screen
     */
    private DialogType dialogType;

    /**
     * UI elements
     */
    private TextField jiraIdField = new TextField();
    private TextField jiraDescripField = new TextField();

    public JiraInputDialog (DialogType dialogType) {
        this.dialogType = dialogType;
        init();
    }

    public JiraInputDialog (String jiraId, String jiraDescription, DialogType dialogType) {
        this.dialogType = dialogType;
        init();
        jiraIdField.setText(jiraId);
        jiraDescripField.setText(jiraDescription);
    }

    /**
     * Initialization of this dialog
     */
    private void init() {
        //Depending on dialogType, choosing buttonType with text and setting title of the dialog
        ButtonType buttonType;
        if (dialogType == DialogType.EDIT) {
            buttonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
            dialog.setTitle("Edit jira");
        } else {
            buttonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.setTitle("Add new jira");
        }

        //Adding new buttonType and retrieving Button instance
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);
        Button addButton = (Button) dialog.getDialogPane().lookupButton(buttonType);

        //Configuring gridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        gridPane.add(new Label("Jira ID:"), 0, 0);
        gridPane.add(jiraIdField, 1, 0);
        gridPane.add(new Label("Jira description:"), 0, 1);
        gridPane.add(jiraDescripField, 1, 1);

        //Configuring dialog
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(gridPane);

        //Setting validator for jira ID field. If field is empty, ID is not valid
        jiraIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.equals("")) {
                idValid = false;
                return;
            }
            idValid = true;
        });

        //Catching the action event (user pushed "Add" or "Edit" button)
        //If jira ID field is not valid, showing alert to user and returning him to the dialog screen
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!idValid) {
                sendAlert("Enter Jira id, please", Alert.AlertType.WARNING);
                event.consume();
            }
        });

        //If jira ID field is valid, creating new JiraPair object and returning it
        //(It will be wrapped into Optional)
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType && idValid) {
                return new JiraPair(jiraIdField.getText(), jiraDescripField.getText());
            }
            return null;
        });
    }

    /**
     * Shows dialog to user and waits for action
     * @return JiraPair wrapped into Optional. JiraPair may be null if user canceled the dialog
     */
    public Optional<JiraPair> showAndWait() {
        return dialog.showAndWait();
    }

    /**
     * Types of dialog for this screen.
     * ADD - dialog for adding new Jira
     * EDIT - dialog for editing existed Jira
     */
    public enum DialogType {
        ADD,
        EDIT
    }
}