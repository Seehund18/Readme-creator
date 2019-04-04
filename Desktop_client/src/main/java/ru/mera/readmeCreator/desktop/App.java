/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import javafx.application.Application;
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
import java.net.URL;

/**
 * Main class of the desktop application
 */
public class App extends Application {
    private WebServiceConnector webServiceConnector;
    private Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void init() throws Exception {
        webServiceConnector = new WebServiceConnector(new URL("http://localhost:8080"));
    }

    @Override
    public void start(Stage stage) {
        //Configuring button
        Button generateButton = new Button("Generate file");
        generateButton.setPrefSize(100, 100);
        generateButton.setOnAction(event -> {
            try {
                //User choose where to save file
                FileChooser saveAs = new FileChooser();
                saveAs.setTitle("Save file as");
                saveAs.setInitialFileName("Hello World.rtf");
                File helloWorldFile = saveAs.showSaveDialog(stage);

                if (helloWorldFile != null) {
                    webServiceConnector.downloadFile("/files/HelloWorld.rtf", helloWorldFile);
                    Alert notify = new Alert(Alert.AlertType.INFORMATION, "Your file has been downloaded", ButtonType.OK);
                    notify.showAndWait();
                } else {
                    Alert wrongFileName = new Alert(Alert.AlertType.WARNING, "Please, specify a file name", ButtonType.OK);
                    wrongFileName.showAndWait();
                }
            } catch (WebServiceConnectorException ex) {
                log.error(ex.getMessage(), ex);
                Alert error = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                error.showAndWait();
            }
        });

        //Configuring borderPane
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
