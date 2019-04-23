package ru.mera.readmeCreator.desktop;/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

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

import java.net.MalformedURLException;
import java.net.URL;

public class MainApp extends Application implements AlertSender {
    /**
     * File connector to the web service
     */
    private static FileWebServiceConnector fileWebServiceConnector;
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

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
    public void start(Stage stage) throws Exception {
        propertiesInit();
        VBox root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));
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

//    /**
//     * Sets connector. If fileWebServiceConnector is not instantiated yet,
//     * creates new connector. Otherwise sets new URL in existed connector
//     * @param serviceURL url of web service
//     */
//    static void setConnector(String serviceURL) throws MalformedURLException {
//        if (fileWebServiceConnector == null) {
//            fileWebServiceConnector = new FileWebServiceConnector(new URL(serviceURL));
//        } else {
//            fileWebServiceConnector.setWebService(serviceURL);
//        }
//    }
//
//    /**
//     * Checks webService availability
//     * @return true if service is available and false otherwise
//     */
//    static boolean checkWebService() {
//        try {
//            return fileWebServiceConnector.isServiceAvailable();
//        } catch (WebServiceConnectorException ex) {
//            log.error(ex.getMessage(), ex);
//            return false;
//        }
//    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
