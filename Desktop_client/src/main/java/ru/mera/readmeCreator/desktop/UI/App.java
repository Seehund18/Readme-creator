/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.UI;

import static ru.mera.readmeCreator.desktop.UI.UiElements.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readmeCreator.desktop.*;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main class of the desktop application
 */
public class App extends Application implements AlertSender {

    /**
     * File connector to the web service
     */
    private static FileWebServiceConnector fileWebServiceConnector;
    private static final Logger log = LoggerFactory.getLogger(App.class);

    static FileWebServiceConnector getFileWebServiceConnector() {
        return fileWebServiceConnector;
    }

    static void setFileWebServiceConnector(FileWebServiceConnector connector) {
        fileWebServiceConnector = connector;
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

    /**
     * Entry point of JavaFX application
     */
    @Override
    public void start(Stage stage) {
        propertiesInit();

        //Adding UrlStatusListener to webServiceUrl, which activates on change of text in the field
        //Every field change will activates validation and a status message near the field will be shown
        webServiceUrl.textProperty().addListener(new UrlStatusListener());

        //Adding handler to "Generate HelloWorld file"
        generateButton.setOnAction(new GenerateButtonHandler(stage));

        //Adding handler to "Submit" button
        submitButton.setOnAction(new SubmitButtonHandler(stage));

        //Configuring layouts
        FlowPane flow;
        flow = new FlowPane(10,0, webServiceUrl, urlStatus);
        flow.setAlignment(Pos.CENTER);
        VBox firstSection = new VBox(10, webServiceLabel, flow, separatLines[0]);
        firstSection.setAlignment(Pos.CENTER);

        flow = new FlowPane(10,0, helloLabel, generateButton);
        flow.setAlignment(Pos.CENTER);
        VBox secondSection = new VBox(10, flow, separatLines[1]);
        secondSection.setAlignment(Pos.CENTER);

        VBox thirdSection = new VBox(10, textToFileLabel, userInput, submitButton);
        thirdSection.setAlignment(Pos.CENTER);

        VBox root = new VBox(10,firstSection, secondSection,thirdSection);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);

        //Configuring stage
        stage.setHeight(500);
        stage.setWidth(500);
        stage.setTitle("Readme generator");
        stage.setScene(scene);
        stage.show();
        setStageAtCenter(stage);

        UiElements.config();
    }

    /**
     * Sets connector. If fileWebServiceConnector is not instantiated yet,
     * creates new connector. Otherwise sets new URL in existed connector
     * @param serviceURL url of web service     *
     */
    static void settingConnector(String serviceURL) throws MalformedURLException {
        if (fileWebServiceConnector == null) {
            fileWebServiceConnector = new FileWebServiceConnector(new URL(serviceURL));
        } else {
            fileWebServiceConnector.setWebService(serviceURL);
        }
    }

    /**
     * Checks webService availability
     * @return true if service is available and false otherwise
     */
    static boolean checkingWebService() {
        try {
            return fileWebServiceConnector.isServiceAvailable();
        } catch (WebServiceConnectorException ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
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

    public static void main(String[] args) {
        Application.launch(args);
    }
}