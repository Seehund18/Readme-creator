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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main class of the desktop application
 */
public class App extends Application implements AlertSender {
    //Connector to the webService
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

    @Override
    public void start(Stage stage) {
        propertiesInit();

        //Adding UrlStatusListener to webServiceUrlField, which activates on change of text in the field
        //Every field change will activates validation and a status message near the field will be shown
        webServiceUrlField.textProperty().addListener(new UrlStatusListener());

        //Adding listener to generateButton, which tries to download file
        generateButton.setOnAction(new GenerateButtonHandler(stage));

        submitButton.setOnAction(new SubmitButtonHandler(stage));

        //Configuring layouts
        //TODO Оптимизировать
        FlowPane flow1 = new FlowPane(10,0, webServiceUrlField, urlStatus);
        flow1.setAlignment(Pos.CENTER);
        VBox firstSection = new VBox(10, webServiceLabel, flow1, separatLines[0]);
        firstSection.setAlignment(Pos.CENTER);

        FlowPane flow2 = new FlowPane(10,0, helloLabel, generateButton);
        flow2.setAlignment(Pos.CENTER);
        VBox secondSection = new VBox(10, flow2, separatLines[1]);
        secondSection.setAlignment(Pos.CENTER);

        VBox thirdSection = new VBox(10, textToFileLabel, textToFile, submitButton);
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
     * creating new Connector otherwise setting URL in existed Connector
     * @param serviceURL url of web service
     * @return true if everything is alright and false if there was an exception
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
    static boolean checkingWebService() throws WebServiceConnectorException {
        return fileWebServiceConnector.isServiceAvailable();
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