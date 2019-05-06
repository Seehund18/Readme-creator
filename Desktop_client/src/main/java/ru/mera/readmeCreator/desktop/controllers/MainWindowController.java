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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readmeCreator.desktop.entities.JiraPair;
import ru.mera.readmeCreator.desktop.entities.UserData;
import ru.mera.readmeCreator.desktop.entities.ValidatedTextField;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;
import ru.mera.readmeCreator.desktop.properties.PropertiesManager;
import ru.mera.readmeCreator.desktop.validators.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for MainWindow.fxml.
 */
public class MainWindowController implements AlertSender {
    private static final Logger log = LoggerFactory.getLogger(MainWindowController.class);

    /**
     * Section for url input. urlField is a part of it
     */
    @FXML
    private HBox urlInputHBox;
    private ValidatedTextField urlField = new ValidatedTextField(new UrlFieldValidator());

    /**
     * Section for parameters input
     */
    @FXML
    private GridPane formGridPane = new GridPane();
    private LinkedHashMap<String, ValidatedTextField> paramFieldMap = new LinkedHashMap<>();

    /**
     * Section for jira table. Table is filled with jira pairs (Jira_ID and Jira_Description) from JiraList
     */
    @FXML
    private TableView<JiraPair> jiraTable = new TableView<>();
    private ObservableList<JiraPair> observableJiraList = FXCollections.observableArrayList();

    @FXML
    private Button submitButton;

    {
        //Filling paramFieldMap
        TextField patchNameField = new TextField();
        patchNameField.setPromptText("Example: AvayaOceana_UAC");
        paramFieldMap.put("patchName",
                new ValidatedTextField(patchNameField, new Text(), new PatchNameFieldValidator()));

        TextField dateField = new TextField();
        dateField.setPromptText("dd/mm/yyyy");
        paramFieldMap.put("date",
                new ValidatedTextField(dateField, new Text(), new DateFieldValidator()));

        TextField updateIdField = new TextField();
        updateIdField.setPromptText("Example: 521002001");
        paramFieldMap.put("updateId",
                new ValidatedTextField(updateIdField, new Text(), new UpdateIdFieldValidator()));

        TextField releaseVerField = new TextField();
        releaseVerField.setPromptText("Example: 3.5.0.1");
        paramFieldMap.put("releaseVersion",
                new ValidatedTextField(releaseVerField, new Text(), new ReleaseVerFieldValidator()));

        TextField issueNumberField = new TextField();
        issueNumberField.setPromptText("Example: 4");
        paramFieldMap.put("issueNumber",
                new ValidatedTextField(issueNumberField, new Text(), new IssueNumFieldValidator()));
    }

    /**
     * Initializes elements, which can't be initialized in MainWindow.fxml file.
     * This method will be automatically called by FXMLLoader {@link Initializable}
     */
    public void initialize() {
        //Initializing url text field. Setting url from config.properties file
        urlField.getTextField().setText(PropertiesManager.getPropertyValue("webServiceURL"));
        urlField.getTextField().setPromptText("Enter URL");

        //Filling urlInputHBox
        urlInputHBox.getChildren().addAll(urlField.getTextField(), urlField.getStatusText());

        //Adding elements to formGridPane
        int rowIndex = 0;
        for (ValidatedTextField row: paramFieldMap.values()) {
            formGridPane.add(row.getTextField(), 1, rowIndex);
            formGridPane.add(row.getStatusText(), 2, rowIndex++);
        }

        //Initializing jiraTable
        TableColumn<JiraPair, String> jiraIdColumn = new TableColumn<>("Jira ID");
        TableColumn<JiraPair, String> jiraDescripColumn = new TableColumn<>("Jira description");
        jiraIdColumn.setCellValueFactory(new PropertyValueFactory<>("jiraId"));
        jiraDescripColumn.setCellValueFactory(new PropertyValueFactory<>("jiraDescrip"));
        jiraTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        jiraTable.getColumns().addAll(jiraIdColumn, jiraDescripColumn);
        jiraTable.setItems(observableJiraList);

        //Adding button handler for "Submit" button
        submitButton.setOnAction(new SubmitButtonHandler(this));
    }

    /**
     * Retrieves information from fields, validates it and packs into userData
     * @return UserData wrapped into Optional. If everything is valid, UserData returned.
     *         If something is invalid, Optional will be empty
     * @throws MalformedURLException URL can't be created
     */
    public Optional<UserData> retrieveUserData() throws MalformedURLException {
        log.info("Validating entered parameters...");
        Optional<String> invalidParam = checkParameters();
        if (invalidParam.isPresent()) {
            log.info("Some fields are invalid:\n{}", invalidParam.get());
            sendAlert("Some fields are invalid:\n\n" + invalidParam.get(), Alert.AlertType.ERROR);
            return Optional.empty();
        }

        log.info("Retrieving entered parameters from fields...");
        URL serviceUrl = new URL(urlField.getTextField().getText());

        //Constructing new map from the paramFieldMap, with the same keys, but the values are the text from the fields
        Map<String, String> paramTextMap = paramFieldMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                                          entry -> entry.getValue().getTextField().getText()));
        return Optional.of(new UserData(serviceUrl, paramTextMap, observableJiraList));
    }

    /**
     * Checks parameters, entered by user.
     * @return String wrapped into Optional. String consists of invalid parameters names.
     *         If everything is valid, empty Optional will be returned.
     */
    private Optional<String> checkParameters() {
        final StringBuilder invalidParam = new StringBuilder();
        if (!urlField.isValid()) {
            invalidParam.append("Url field\n");
        }
        if (observableJiraList.isEmpty()) {
            invalidParam.append("Jira table is empty\n");
        }
        paramFieldMap.forEach((key, value) -> {
            if (!value.isValid()) {
                invalidParam.append(key).append("\n");
            }
        });

        if (invalidParam.length() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(invalidParam.toString());
        }
    }

    /**
     * Handler for "+" table button which adds new jiraPair to the table
     * @param e {@link Event}
     */
    @FXML
    private void addJira(Event e) {
        JiraInputDialog jiraDialog = new JiraInputDialog(JiraInputDialog.DialogType.ADD);
        Optional<JiraPair> jiraPair = jiraDialog.showAndWait();
        jiraPair.ifPresent(pair -> {
            if (observableJiraList.contains(pair)) {
                sendAlert("Such jira already exists", Alert.AlertType.WARNING);
                return;
            }
            observableJiraList.add(pair);
        });
    }

    /**
     * Handler for "-" table button which removes selected jiraPair from the table
     * @param e {@link Event}
     */
    @FXML
    private void removeJira(Event e) {
        int removeIndex = jiraTable.getFocusModel().getFocusedIndex();
        if (removeIndex < 0) {
            //Table is empty. There is nothing to remove
            return;
        }
        observableJiraList.remove(removeIndex);
    }

    /**
     * Handler for "Edit" table button which edits selected jiraPair in the table
     * @param e {@link Event}
     */
    @FXML
    private void editJira(Event e) {
        int editIndex = jiraTable.getFocusModel().getFocusedIndex();
        if (editIndex < 0) {
            //Table is empty. There is nothing to edit
            return;
        }

        JiraPair editPair = observableJiraList.get(editIndex);
        JiraInputDialog jiraDialog = new JiraInputDialog(editPair.getJiraId(),
                                                         editPair.getJiraDescrip(),
                                                         JiraInputDialog.DialogType.EDIT);
        Optional<JiraPair> jiraPair = jiraDialog.showAndWait();
        jiraPair.ifPresent(pair -> {
            //Alerting user if the jiraId already exists in the table
            if (!pair.equals(editPair) && observableJiraList.contains(pair)) {
                sendAlert("Such jira already exists", Alert.AlertType.WARNING);
                return;
            }
            observableJiraList.set(editIndex, pair);
        });
    }
}