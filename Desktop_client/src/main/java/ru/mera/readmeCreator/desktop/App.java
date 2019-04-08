/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main class of the desktop application
 */
public class App extends Application {
    private WebServiceConnector webServiceConnector;
    private final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage stage) {
        webServiceInit();

        //Configuring button
        Button generateButton = new Button("Generate file");
        generateButton.setPrefSize(100, 100);
        generateButton.setOnAction(event -> {
            //Configuring 'save as' window
            FileChooser saveAs = new FileChooser();
            saveAs.setTitle("Save file as");
            saveAs.setInitialFileName("Hello World.rtf");
            saveAs.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("RTF", "*.rtf")
            );
            saveAs.setInitialDirectory(new File(System.getProperty("user.home")));
            File helloWorldFile = saveAs.showSaveDialog(stage);

            try {
                if (helloWorldFile != null) {
                    webServiceConnector.downloadFile("/files/HelloWorld.rtf", helloWorldFile);
                    showAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
                    log.info("File has been downloaded");
                } else {
                    log.info("File downloading was canceled by user");
                }
            } catch (WebServiceConnectorException ex) {
                log.error(ex.getMessage(), ex);
                showAlert(ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        //Configuring root element
        BorderPane root = new BorderPane(generateButton);
        root.setCenter(generateButton);
        Scene scene = new Scene(root);

        //Configuring stage
        stage.setHeight(500);
        stage.setWidth(500);
        stage.setTitle("Readme generator");
        stage.setScene(scene);
        stage.show();
        setStageAtCenter(stage);
    }

    //Tries to initialize web service from property file
    private void webServiceInit() {
        URL webServiceURL = null;
        try {
            webServiceURL = new URL(PropertiesManager.getPropertyValue("webServiceURL"));
        } catch (ExceptionInInitializerError | MalformedURLException ex) {
            String errorText;
            if(ex.getCause().getCause() instanceof FileNotFoundException) {
                //FileNotFoundException was the cause
                errorText = "No config file was found. Maybe it was deleted or moved";
            } else if (ex.getCause().getCause() instanceof IOException) {
                //IOException was the cause
                errorText = "Exception occurred while reading config file";
            } else {
                //MalformedURLException was the cause
                errorText = "Can't create URL of web service. Maybe config.properties file was corrupted";
            }
            log.error(errorText, ex);
            showAlert(errorText, Alert.AlertType.ERROR);
            Platform.exit();
        }
        webServiceConnector = new WebServiceConnector(webServiceURL);
    }

    //Creates and shows alert to user
    private void showAlert(String text, Alert.AlertType type) {
        Alert alert = new Alert(type, text, ButtonType.OK);
        alert.showAndWait();
    }

    //Sets stage at the screen center. Method stage.centerOnScreen() doesn't seem to work properly
    private void setStageAtCenter(Stage stage) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}