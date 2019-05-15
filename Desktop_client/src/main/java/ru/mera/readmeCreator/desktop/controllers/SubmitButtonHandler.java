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
import ru.mera.readmeCreator.desktop.entities.UserData;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;
import ru.mera.readmeCreator.desktop.properties.PropertiesManager;
import ru.mera.readmeCreator.desktop.properties.PropertiesManagerException;
import ru.mera.readmeCreator.desktop.webService.WebServiceException;
import ru.mera.readmeCreator.desktop.webService.WebServiceManager;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;

/**
 * Handler for "Submit" button
 */
class SubmitButtonHandler implements EventHandler<ActionEvent>, AlertSender {
    private static Logger log = LoggerFactory.getLogger(SubmitButtonHandler.class);

    /**
     * Controller of mainWindow through which user data can be retrieved
     */
    private MainWindowController controller;

    /**
     * Save as dialog window
     */
    private FileChooser saveAs = new FileChooser();


    {
        //Configuring saveAs
        saveAs.setTitle("Save file as");
        saveAs.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("RTF", "*.rtf"));
        saveAs.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    SubmitButtonHandler(MainWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(ActionEvent event) {
        //Retrieving current stage
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        //Retrieving userData from the controller
        UserData userData;
        try {
            Optional<UserData> result = controller.retrieveUserData();
            if (!result.isPresent()) {
                return;
            }
            userData = result.get();
        } catch (MalformedURLException ex) {
            log.error("Can't create URL of web service", ex);
            sendAlert("Can't create URL of web service", Alert.AlertType.ERROR);
            return;
        }

        //Setting and checking web service url
        WebServiceManager.setService(userData.getWebServiceUrl());
        if (!WebServiceManager.checkWebService()) {
            log.info("Service is unavailable");
            sendAlert("Service is unavailable right now. Try again later", Alert.AlertType.WARNING);
            return;
        }

        //Constructing file name and invoking 'save as' dialog
        String fileName = userData.getParamMap().get("updateId") + "_Patch_Readme.rtf";
        saveAs.setInitialFileName(fileName);
        File userDataFile = saveAs.showSaveDialog(stage);

        if (userDataFile != null) {
            //User decided where to save file
            try {
                //Trying to download file
                WebServiceManager.downloadFile("/files/" + fileName, userData.toString(), userDataFile);
                sendAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
                log.info("File has been downloaded");

                //If download was successful, changing default value of webServiceURL in property file
                PropertiesManager.setPropertyValue("webServiceURL", userData.getWebServiceUrl().toString());
            } catch (PropertiesManagerException | WebServiceException ex) {
                sendAlert(ex.getMessage(), Alert.AlertType.ERROR);
                log.error(ex.getMessage(), ex);
            }
        } else {
            //User canceled 'save as' dialog
            log.info("File downloading was canceled by user");
        }
    }
}