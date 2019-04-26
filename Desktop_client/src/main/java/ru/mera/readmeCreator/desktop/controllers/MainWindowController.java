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
import ru.mera.readmeCreator.desktop.validators.DateValidator;
import ru.mera.readmeCreator.desktop.validators.UrlFieldValidator;

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
    private HBox urlInput;
    private ValidatedTextField urlField = new ValidatedTextField(new UrlFieldValidator());

    /**
     * Section for parameters input form. formParameters consists of sequence of fields
     */
    @FXML
    private GridPane form = new GridPane();
    private LinkedHashMap<String, ValidatedTextField> formParameters = new LinkedHashMap<>();

    /**
     * Section for jira table. Table is filled with jira pairs (Jira_ID and Jira_Description) from jiraList
     */
    @FXML
    private TableView<JiraPair> jiraTable = new TableView<>();
    private ObservableList<JiraPair> jiraList = FXCollections.observableArrayList();

    @FXML
    private Button submitButton;

    {
        formParameters.put("patchName", new ValidatedTextField());
        TextField date = new TextField();
        date.setPromptText("dd/mm/yy");
        formParameters.put("date", new ValidatedTextField(date, new Text(), new DateValidator()));
        formParameters.put("updateId", new ValidatedTextField());
        formParameters.put("releaseVersion", new ValidatedTextField());
    }

    /**
     * Initializes elements, which can't be initialized in MainWindow.fxml file.
     * This method will be automatically called by FXMLLoader {@link Initializable}
     */
    public void initialize() {
        //Initializing url text field. Setting url from config.properties file
        urlField.getTextField().setText(PropertiesManager.getPropertyValue("webServiceURL"));
        urlField.getTextField().setPromptText("Enter URL");

        //Filling urlInput
        urlInput.getChildren().addAll(urlField.getTextField(), urlField.getStatusText());

        //Adding elements to form
        int rowIndex = 0;
        for (ValidatedTextField row: formParameters.values()) {
            form.add(row.getTextField(), 1, rowIndex);
            form.add(row.getStatusText(), 2, rowIndex++);
        }

        //Initializing jiraTable
        TableColumn<JiraPair, String> jiraIdColumn = new TableColumn<>("Jira ID");
        TableColumn<JiraPair, String> jiraDescripColumn = new TableColumn<>("Jira description");
        jiraIdColumn.setCellValueFactory(new PropertyValueFactory<>("jiraId"));
        jiraDescripColumn.setCellValueFactory(new PropertyValueFactory<>("jiraDescrip"));
        jiraTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        jiraTable.getColumns().addAll(jiraIdColumn, jiraDescripColumn);
        jiraTable.setItems(jiraList);

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
        //Constructing new parameters map from formParameters
        Map<String, String> parameters = formParameters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                                          entry -> entry.getValue().getTextField().getText()));
        return Optional.of(new UserData(serviceUrl, parameters, jiraList));
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
        if (jiraList.isEmpty()) {
            invalidParam.append("Jira table is empty\n");
        }
        formParameters.forEach((key, value) -> {
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
    private void removeJira(Event e) {
        int removeIndex = jiraTable.getFocusModel().getFocusedIndex();
        if (removeIndex < 0) {
            //Table is empty. There is nothing to remove
            return;
        }
        jiraList.remove(removeIndex);
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