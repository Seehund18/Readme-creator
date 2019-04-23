/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.mera.readmeCreator.desktop.JiraInputDialog;
import ru.mera.readmeCreator.desktop.JiraPair;
import ru.mera.readmeCreator.desktop.PropertiesManager;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;

import java.util.Optional;

/**
 * Controller for MainWindow.fxml.
 */
public class MainWindowController implements AlertSender {
    @FXML
    private TextField webServiceUrl;

    @FXML
    private Text urlStatus;

    @FXML
    private GridPane form = new GridPane();
    private ObservableMap<String, TextField> formElements = FXCollections.observableHashMap();
    private ObservableMap<String, Text> formElemStatuses = FXCollections.observableHashMap();

    @FXML
    private TableView<JiraPair> jiraTable = new TableView<>();
    private ObservableList<JiraPair> jiraList = FXCollections.observableArrayList();

    @FXML
    private Button submitButton;

    {
        formElements.put("patchName", new TextField());
        formElements.put("date", new TextField());
        formElements.put("updateId", new TextField());
        formElements.put("releaseVer", new TextField());

        formElemStatuses.put("patchNameStatus", new Text());
        formElemStatuses.put("dateStatus", new Text());
        formElemStatuses.put("updateIdStatus", new Text());
        formElemStatuses.put("releaseVerStatus", new Text());
    }

    /**
     * Initializes elements, which can't be initialized in MainWindow.fxml file.
     * This method will be automatically called by FXMLLoader {@link Initializable}
     */
    public void initialize() {
        webServiceUrl.setText(PropertiesManager.getPropertyValue("webServiceURL"));

        //Adding UrlStatusListener to webServiceUrl, which will validate it
        //and show validation status to user by editing urlStatus
        webServiceUrl.textProperty().addListener(new UrlStatusListener(urlStatus));

        formElemStatuses.values()
                .forEach(statusText -> {
                    statusText.setText("It's alright");
                    statusText.setFill(Color.GREEN);
                });

        form.addColumn(1, formElements.values().toArray(new Node[0]));
        form.addColumn(2, formElemStatuses.values().toArray(new Node[0]));

        TableColumn<JiraPair, String> jiraIdColumn = new TableColumn<>("Jira ID");
        TableColumn<JiraPair, String> jiraDescripColumn = new TableColumn<>("Jira description");
        jiraIdColumn.setCellValueFactory(new PropertyValueFactory<>("jiraId"));
        jiraDescripColumn.setCellValueFactory(new PropertyValueFactory<>("jiraDescrip"));
        jiraTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        jiraTable.getColumns().addAll(jiraIdColumn, jiraDescripColumn);
        jiraTable.setItems(jiraList);

        //Adding button handler for "Submit" button
        submitButton.setOnAction(new SubmitButtonHandler());
    }

    /**
     * Handler for "+" table button which adds new jiraPair to the table
     * @param e {@link Event}
     */
    @FXML
    public void addJira(Event e) {
        JiraInputDialog jiraDialog = new JiraInputDialog(JiraInputDialog.DialogType.ADD);
        Optional<JiraPair> jiraPair = jiraDialog.showAndWait();
        jiraPair.ifPresent(pair -> {
            if (jiraList.contains(pair)) {
                sendAlert("Such jira already exists", Alert.AlertType.WARNING);
                return;
            }
            jiraList.add(pair);
        });
    }

    /**
     * Handler for "-" table button which removes selected jiraPair from the table
     * @param e {@link Event}
     */
    @FXML
    public void removeJira(Event e) {
        int removeIndex = jiraTable.getFocusModel().getFocusedIndex();
        if (removeIndex >= 0) {
            //Table is empty. There is nothing to remove
            jiraList.remove(removeIndex);
        }
    }

    /**
     * Handler for "Edit" table button which edits selected jiraPair in the table
     * @param e {@link Event}
     */
    @FXML
    public void editJira(Event e) {
        int editIndex = jiraTable.getFocusModel().getFocusedIndex();
        if (editIndex < 0) {
            //Table is empty. There is nothing to edit
            return;
        }
        JiraPair editPair = jiraList.get(editIndex);

        JiraInputDialog jiraDialog = new JiraInputDialog(editPair.getJiraId(),
                editPair.getJiraDescrip(),
                JiraInputDialog.DialogType.EDIT);
        Optional<JiraPair> jiraPair = jiraDialog.showAndWait();
        jiraPair.ifPresent(pair -> {
            //Alerting user if the jiraId already exists in the table
            if (!pair.equals(editPair) && jiraList.contains(pair)) {
                sendAlert("Such jira already exists", Alert.AlertType.WARNING);
                return;
            }
            jiraList.set(editIndex, pair);
        });
    }
}
