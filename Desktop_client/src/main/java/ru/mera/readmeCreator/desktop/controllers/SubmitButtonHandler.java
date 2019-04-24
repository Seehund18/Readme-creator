/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readmeCreator.desktop.*;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;

/**
 * Handler for "Submit" button
 */
class SubmitButtonHandler implements EventHandler<ActionEvent>, AlertSender {
    private MainWindowController controller;

    /**
     * Validator for userInput field
     */
//    private UserInputValidator userInputValidator = new UserInputValidator();
    private static Logger log = LoggerFactory.getLogger(SubmitButtonHandler.class);
    private FileChooser saveAs = new FileChooser();

    {
        saveAs.setTitle("Save file as");
        saveAs.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("RTF", "*.rtf"));
        saveAs.setInitialFileName("User_data.rtf");
        saveAs.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    SubmitButtonHandler(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(ActionEvent event) {
        //Retrieving current stage
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        try {
            controller.retrieveUserData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //        UserData userData;
//        try {
//            Optional<UserData> result = controller.retrieveUserData();
//            if (!result.isPresent()) {
//                return;
//            }
//            userData = result.get();
//        } catch (MalformedURLException ex) {
//            log.error("Can't create URL of web service", ex);
//            sendAlert("Can't create URL of web service", Alert.AlertType.ERROR);
//            return;
//        }
//
//        System.out.println(userData);


//        //Setting and checking web service url
//        String serviceURL = webServiceUrl.getText();
//        try {
//            App.setConnector(serviceURL);
//        } catch (MalformedURLException e) {
//            sendAlert("Can't create web service url", Alert.AlertType.ERROR);
//            log.error("Can't create web service url", e);
//            return;
//        }
//        if (!App.checkWebService()) {
//            sendAlert("Service is unavailable right now. Try again later", Alert.AlertType.WARNING);
//            return;
//        }
//
//        //Invoking 'save as' dialog and choosing place to save file
//        saveAs.setInitialFileName("User_data.rtf");
//        File userDataFile = saveAs.showSaveDialog(stage);
//
//        if (userDataFile != null) {
//            //User decided where to save file
//            try {
//                //Trying to download file
//                App.getFileWebServiceConnector().downloadFile("/files/User_data.rtf", userData.toString(), userDataFile);
//                sendAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
//                log.info("File has been downloaded");
//
//                //If download was successful, changing default value of webServiceURL in property file
//                PropertiesManager.setPropertyValue("webServiceURL", serviceURL);
//            } catch (PropertiesManagerException | WebServiceConnectorException ex) {
//                sendAlert(ex.getMessage(), Alert.AlertType.ERROR);
//                log.error(ex.getMessage(), ex);
//            }
//        } else {
//            //User canceled 'save as' dialog
//            log.info("File downloading was canceled by user");
//        }
    }
}