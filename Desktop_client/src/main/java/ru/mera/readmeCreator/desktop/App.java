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
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Main class of the desktop application
 */
public class App extends Application {
    //UI elements
    private Label webServiceText = new Label("Please, enter a web service URI.\n"
            + "Examples: http://localhost:8080, http://myService.com");
    private TextField webServiceUrlField = new TextField();
    private Text urlStatus = new Text();
    private Button generateButton = new Button("Generate file");
    private FileChooser saveAs = new FileChooser();

    //Flag which indicates, that Url in webServiceUrlField is valid
    private boolean isUrlValid = true;
    //Connector to the webService
    private FileWebServiceConnector fileWebServiceConnector;
    private final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * Initialization of propertiesManager
     */
    private void propertiesInit() {
        try {
            PropertiesManager.init();
        } catch (PropertiesManagerException ex) {
            //If there is an exception, alerting user and exit the program
            log.error(ex.getMessage(), ex);
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
            Platform.exit();
        }
    }

    /**
     * Initialization of UI elements. Sets basic text, size, font and configuration
     */
    private void uiElementsInit() {
        webServiceText.setFont(new Font(14));
        webServiceText.setPrefWidth(350);
        webServiceText.setTextAlignment(TextAlignment.CENTER);
        webServiceText.setWrapText(true);
        webServiceText.setLabelFor(webServiceUrlField);

        webServiceUrlField.setFont(new Font(13));
        webServiceUrlField.setPromptText("Enter URI of web service...");
        webServiceUrlField.setText(PropertiesManager.getPropertyValue("webServiceURL"));
        webServiceUrlField.setMaxSize(200,10);

        generateButton.setPrefSize(100, 10);

        urlStatus.setText("Valid URL");
        urlStatus.setFill(Color.GREEN);

        saveAs.setTitle("Save file as");
        saveAs.setInitialFileName("Hello World.rtf");
        saveAs.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("RTF", "*.rtf")
        );
        saveAs.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    @Override
    public void start(Stage stage) {
        //Initialization
        propertiesInit();
        uiElementsInit();

        //Adding UrlStatusListener to webServiceUrlField, which activates on change of text in the field
        //Every field change will activates validation and a status message near the field will be shown
        webServiceUrlField.textProperty().addListener(new UrlStatusListener());

        //Adding listener to generateButton, which tries to download file
        generateButton.setOnAction(new GenerateButtonHandler(stage));

        //Configuring layouts
        FlowPane flow = new FlowPane(10,0,webServiceUrlField, urlStatus);
        flow.setAlignment(Pos.CENTER);
        VBox verBox = new VBox(10, webServiceText, flow, generateButton);
        verBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(verBox);

        //Configuring stage
        stage.setHeight(500);
        stage.setWidth(500);
        stage.setTitle("Readme generator");
        stage.setScene(scene);
        stage.show();
        setStageAtCenter(stage);
    }

    /**
     * Listener for webServiceUrlField
     */
    private class UrlStatusListener implements ValidatedChangeListener {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if(isValid(newValue)) {
                isUrlValid = true;
                urlStatus.setText("Valid URL");
                urlStatus.setFill(Color.GREEN);
                return;
            }
            isUrlValid = false;
            urlStatus.setText("Not valid URL");
            urlStatus.setFill(Color.RED);
        }

        @Override
        public boolean isValid(String url) {
            //Checking that url is the server url without path to any resource.
            //For that, using regex pattern, which checks that between beginning and ending of the string are only 2 slashes '/'.
            //For example: http://myService.ru is correct (2 slashes); http://myService.ru/files/HelloWorld.rtf is wrong (4 slashes)
            if(!url.matches("^http://[^/]+$")) {
                return false;
            }

            //Using UrlValidator class from apache.commons library to check the url
            UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
            return urlValidator.isValid(url);
        }
    }

    /**
     * Handler for generateButton
     */
    private class GenerateButtonHandler implements EventHandler<ActionEvent> {
        private Stage stage;

        private GenerateButtonHandler(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(ActionEvent event) {
            //Checking flag
            if (!isUrlValid) {
                showAlert("URL is not valid", Alert.AlertType.ERROR);
                return;
            }

            String serviceURL = webServiceUrlField.getText();
            if (!settingConnector(serviceURL) || !checkingWebService()) {
                return;
            }

            //Invoking 'save as' dialog and choosing place to save file
            File helloWorldFile = saveAs.showSaveDialog(stage);

            //If user choose where to save file, then try to download file from service
            if (helloWorldFile != null) {
                try {
                    //Trying to download file
                    fileWebServiceConnector.downloadFile("/files/Hello_world.rtf", helloWorldFile);
                    showAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
                    log.info("File has been downloaded");

                    //If download was successful, changing default value of webServiceURL in property file
                    PropertiesManager.setPropertyValue("webServiceURL", serviceURL);
                } catch (PropertiesManagerException | WebServiceConnectorException ex) {
                    showAlert(ex.getMessage(), Alert.AlertType.ERROR);
                    log.error(ex.getMessage(), ex);
                }
            } else {
                //User canceled 'save as' dialog
                log.info("File downloading was canceled by user");
            }
        }

        /**
         * Sets connector. If fileWebServiceConnector is not instantiated yet,
         * creating new Connector otherwise setting URL in existed Connector
         * @param serviceURL url of web service
         * @return true if everything is alright and false if there was an exception
         */
        private boolean settingConnector(String serviceURL) {
            try {
                if (fileWebServiceConnector == null) {
                    fileWebServiceConnector = new FileWebServiceConnector(new URL(serviceURL));
                } else {
                    fileWebServiceConnector.setWebService(serviceURL);
                }
                return true;
            } catch (MalformedURLException ex) {
                log.error("Can't create URL of web service", ex);
                showAlert("Can't create URL of web service", Alert.AlertType.ERROR);
                return false;
            }
        }

        /**
         * Checks webService availability
         * @return true if service is available and false otherwise
         */
        private boolean checkingWebService() {
            try {
                if (fileWebServiceConnector.isServiceAvailable()) {
                    return true;
                } else {
                    showAlert("This service is unavailable. Try again later", Alert.AlertType.WARNING);
                    return false;
                }
            } catch (WebServiceConnectorException e) {
                log.error(e.getMessage(), e);
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
                return false;
            }
        }
    }

    /**
     * Creates and shows alert to user
     * @param text text to show
     * @param type type of alert
     */
    private void showAlert(String text, Alert.AlertType type) {
        Alert alert = new Alert(type, text, ButtonType.OK);
        alert.showAndWait();
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