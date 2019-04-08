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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
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
    private Text webServiceFieldStatus = new Text();
    private Button generateButton = new Button("Generate file");
    private FileChooser saveAs = new FileChooser();

    //Flag which indicates, that Url in webServiceUrlField is valid
    private boolean isUrlValid = true;
    private WebServiceConnector webServiceConnector;
    private final Logger log = LoggerFactory.getLogger(App.class);

    //Initialization of propertiesManager
    private void propertiesInit() {
        try {
            PropertiesManager.init();
        } catch (PropertiesManagerException ex) {
            log.error(ex.getMessage(), ex);
            showAlert(ex.getMessage(), Alert.AlertType.ERROR);
            Platform.exit();
        }
    }

    //Initialization of UI elements. Sets default text, size and font
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

        webServiceFieldStatus.setText("Valid URL");
        webServiceFieldStatus.setFill(Color.GREEN);

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

        //Adding new ValidUrlListener to webServiceUrlField, which activates on change of text in the field
        webServiceUrlField.textProperty().addListener(new ValidUrlChangeListener() {
            //Validates URL in the field and sets appropriate text and color in webServiceFieldStatus
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(isValid(newValue)) {
                    webServiceFieldStatus.setText("Valid URL");
                    webServiceFieldStatus.setFill(Color.GREEN);
                    isUrlValid = true;
                    return;
                }
                isUrlValid = false;
                webServiceFieldStatus.setText("Not valid URL");
                webServiceFieldStatus.setFill(Color.RED);
            }
        });

        //Adding listener to generateButton, which activates on button push
        generateButton.setOnAction(event -> {
            String serviceURL = webServiceUrlField.getText();
            //Checking flag
            if (!isUrlValid) {
                showAlert("URL is not valid", Alert.AlertType.ERROR);
                return;
            }

            //If webServiceConnector is not instantiated yet
            //or serviceURL is different from the existed one, creates new Connector
            if (webServiceConnector == null || !webServiceConnector.isUrlEqual(serviceURL)) {
                try {
                    webServiceConnector = new WebServiceConnector(new URL(serviceURL));
                } catch (MalformedURLException ex) {
                    log.error("Can't create URL of web service", ex);
                    showAlert("Can't create URL of web service", Alert.AlertType.ERROR);
                }
            }

            //Invoking 'save as' dialog and choosing place to save file
            File helloWorldFile = saveAs.showSaveDialog(stage);

            //If user choose where to save file, then try to download file from service
            if (helloWorldFile != null) {
                try {
                    //Trying to download file
                    webServiceConnector.downloadFile("/files/HelloWorld.rtf", helloWorldFile);
                    showAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
                    log.info("File has been downloaded");

                    //If download was successful, changing default value of webServiceURL
                    PropertiesManager.setPropertyValue("webServiceURL", serviceURL);
                } catch (PropertiesManagerException | WebServiceConnectorException ex) {
                    showAlert(ex.getMessage(), Alert.AlertType.ERROR);
                    log.error(ex.getMessage(), ex);
                }
            } else {
                //User canceled 'save as' dialog
                log.info("File downloading was canceled by user");
            }
        });

        //Configuring layouts
        FlowPane flow = new FlowPane(10,0,webServiceUrlField, webServiceFieldStatus);
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