/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;

import java.util.Optional;

/**
 * Dialog, which is shown when user pressed "+" button.
 * User can write Jira ID and jira description and when this pair will be added to the table
 * or he can cancel this dialog
 */
public class JiraInputDialog implements AlertSender {

    /**
     * Skeleton of this dialog to which actions are delegated
     */
    private Dialog<JiraPair> dialog = new Dialog<>();

    /**
     * Flag indicated whether jira ID is valid or not
     */
    private boolean isIdValid;

    //UI elements
    private TextField jiraIdField = new TextField();
    private TextField jiraDecripField = new TextField();

    {
        //Adding new button type to a dialog and getting addButton instance
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);

        //Configuring gridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Jira ID:"), 0, 0);
        grid.add(jiraIdField, 1, 0);
        grid.add(new Label("Jira description:"), 0, 1);
        grid.add(jiraDecripField, 1, 1);

        //Configuring dialog
        dialog.setTitle("Add new jira");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(grid);

        //Setting validator for jira ID field. If field is empty, ID is not valid
        jiraIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.equals("")) {
                isIdValid = false;
                return;
            }
            isIdValid = true;
        });

        //Catching the action event (user pushed "add" button)
        //If jira ID field is not valid, showing alert to user and return him to dialog
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
           if (!isIdValid) {
               sendAlert("Enter Jira id, please", Alert.AlertType.WARNING);
               event.consume();
           }
        });

        //If jira ID field is valid, creating new JiraPair object and returning it
        //(It will be wrapped into Optional)
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (isIdValid) {
                    return new JiraPair(jiraIdField.getText(), jiraDecripField.getText());
                }
            }
            return null;
        });
    }

    /**
     * Shows dialog to user and waits for action
     * @return JiraPair wrapped into Optional. JiraPair maybe null if user canceled the dialog
     */
    public Optional<JiraPair> showAndWait() {
        return dialog.showAndWait();
    }
}