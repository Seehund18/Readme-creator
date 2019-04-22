/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import ru.mera.readmeCreator.desktop.JiraPair;
import ru.mera.readmeCreator.desktop.PropertiesManager;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * Ui elements of the app's main window
 */
class UiElements {
    //Url input segment
    static Label webServiceLabel = new Label("Please, enter URL of web service.\n"
            + "Examples: http://localhost:8080, http://myService.com");
    static TextField webServiceUrl = new TextField();
    static Text urlStatus = new Text();

    //Readme parameters segment
    static Label parametersLabel = new Label("Enter parameters for readme file:");
//    static Label patchNameLabel = new Label("Patch name:");
//    static TextField patchName = new TextField();
//    static Label dateLabel = new Label("Date:");
//    static TextField date = new TextField();
//    static Label updateIdLabel = new Label("Update ID");
//    static TextField updateId = new TextField();
//    static Label releaseVerLabel = new Label("Release version");
//    static TextField releaseVer = new TextField();
//    static Label jiraIdLabel = new Label("Jira ID");
//    static TextField jiraId = new TextField();
//    static Label jiraDescriptLabel = new Label("Jira description");
//    static TextField jiraDescript = new TextField();
    static LinkedHashMap<String, Label> formElemLabels = new LinkedHashMap<>();
    static LinkedHashMap<String, TextField> formElements = new LinkedHashMap<>();
    static LinkedHashMap<String, Text> formElemStatuses = new LinkedHashMap<>();
    static TableView<JiraPair> table = new TableView<>();
    static LinkedHashMap<String, Button> tableButtons = new LinkedHashMap<>();
    static Button submitButton = new Button("Submit");

    static Line separateLine = new Line();
    static FileChooser saveAs = new FileChooser();

    static {
        formElemLabels.put("patchNameLabel", new Label("Patch name:"));
        formElemLabels.put("dateLabel", new Label("Date:"));
        formElemLabels.put("updateIdLabel", new Label("Update ID:"));
        formElemLabels.put("releaseVerLabel", new Label("Release version:"));

        formElements.put("patchName", new TextField());
        formElements.put("date", new TextField());
        formElements.put("updateId", new TextField());
        formElements.put("releaseVer", new TextField());

        formElemStatuses.put("patchNameStatus", new Text());
        formElemStatuses.put("dateStatus", new Text());
        formElemStatuses.put("updateIdStatus", new Text());
        formElemStatuses.put("releaseVerStatus", new Text());

        tableButtons.put("+", new Button("+"));
        tableButtons.put("-", new Button("-"));
        tableButtons.put("edit", new Button("Edit"));
    }


    /**
     * Configuration of the UI elements. Sets basic text, size, font and configuration
     */
    static void config() {

        webServiceLabel.setFont(new Font(14));
        webServiceLabel.setPrefWidth(350);
        webServiceLabel.setTextAlignment(TextAlignment.CENTER);
        webServiceLabel.setWrapText(true);
        webServiceLabel.setLabelFor(webServiceUrl);

        webServiceUrl.setFont(new Font(13));
        webServiceUrl.setPromptText("Enter URL of web service...");
        webServiceUrl.setText(PropertiesManager.getPropertyValue("webServiceURL"));
        webServiceUrl.setPrefSize(200,10);

        parametersLabel.setFont(new Font(14));
        formElemLabels.values()
                .forEach(label -> label.setFont(new Font(14)));
        formElements.values()
                .forEach(field -> field.setFont(new Font(13)));
        formElemStatuses.values()
                .forEach(statusText -> {
                    statusText.setText("It's alright");
                    statusText.setFill(Color.GREEN);
                });

        //Table configuration
        ObservableList<JiraPair> people  = FXCollections.observableArrayList(
//                new JiraPair("Alala", "34678696976978698759875496497654986598759564646959595"),
//                new JiraPair("UNIDESK-14682", "Add page push url event to Widget Framework"),
//                new JiraPair("Sam", "28"),
//                new JiraPair("Alice", "29"),
//                new JiraPair("Alice", "29"),
//                new JiraPair("Alice", "29"),
//                new JiraPair("Alice", "29"),
//                new JiraPair("Alice", "29")
        );
        TableColumn<JiraPair, String> jiraIdColumn = new TableColumn<>("Jira ID");
        jiraIdColumn.setCellValueFactory(new PropertyValueFactory<>("jiraId"));
        TableColumn<JiraPair, String> jiraDescripColumn = new TableColumn<>("Jira description");
        jiraDescripColumn.setCellValueFactory(new PropertyValueFactory<>("jiraDescrip"));
        table.setPrefWidth(500);
        table.setPrefHeight(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(jiraIdColumn, jiraDescripColumn);
        table.resizeColumn(jiraIdColumn, -100);
        table.setItems(people);

        //Table buttons configuring
        tableButtons.values()
                .forEach(button -> {
                    button.setMinSize(40, 25);
                    button.setFont(new Font(13));
                });

        //Save as window configuring
        saveAs.setTitle("Save file as");
        saveAs.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("RTF", "*.rtf"));
        saveAs.setInitialDirectory(new File(System.getProperty("user.home")));

        //Separate line configuring
        separateLine.setEndX(separateLine.getScene().getWidth());
    }
}