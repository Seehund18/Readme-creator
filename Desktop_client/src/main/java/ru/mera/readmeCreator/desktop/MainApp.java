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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;
import ru.mera.readmeCreator.desktop.properties.PropertiesManager;
import ru.mera.readmeCreator.desktop.properties.PropertiesManagerException;

import java.io.IOException;

/**
 * Main class of the program
 */
public class MainApp extends Application implements AlertSender {
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Initialization of propertiesManager
     */
    private void propertiesInit() {
        try {
            PropertiesManager.init();
        } catch (PropertiesManagerException ex) {
            //If there is an exception, alerting user and exit the program
            log.error(ex.getMessage(), ex);
            sendAlert(ex.getMessage(), Alert.AlertType.ERROR);
            Platform.exit();
        }
    }

    @Override
    public void start(Stage stage) {
        propertiesInit();

        //Loading of MainWindow.fxml
        VBox root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));
        } catch (IOException ex) {
            log.error("Can't load MainWindow.fxml file", ex);
            sendAlert("Can't load MainWindow.fxml file", Alert.AlertType.ERROR);
            return;
        }
        Scene scene = new Scene(root);

        //Configuring stage
        stage.setHeight(600);
        stage.setWidth(600);
        stage.setTitle("Readme generator");
        stage.setScene(scene);
        stage.show();
        setStageAtCenter(stage);
    }

    /**
     * Sets stage at the screen center. Method stage.centerOnScreen() doesn't seem to work properly
     * @param stage stage which needed to be centered
     */
    private void setStageAtCenter(Stage stage) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
