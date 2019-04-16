/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readmeCreator.desktop.*;
import ru.mera.readmeCreator.desktop.interfaces.AlertSender;

import java.io.File;
import java.net.MalformedURLException;

import static ru.mera.readmeCreator.desktop.UI.UiElements.saveAs;
import static ru.mera.readmeCreator.desktop.UI.UiElements.webServiceUrl;

/**
 * Button handler for "Generate HelloWorld" button
 */
class GenerateButtonHandler implements EventHandler<ActionEvent>, AlertSender {
    private Stage stage;
    private Logger log = LoggerFactory.getLogger(GenerateButtonHandler.class);

    GenerateButtonHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        //Checking flag
        if (!UrlStatusListener.isUrlValid()) {
            sendAlert("URL is not valid", Alert.AlertType.ERROR);
            return;
        }

        //Setting and checking web service url
        String serviceURL = webServiceUrl.getText();
        try {
            App.settingConnector(serviceURL);
        } catch (MalformedURLException e) {
            sendAlert("Can't create web service url", Alert.AlertType.ERROR);
            log.error("Can't create web service url", e);
            return;
        }
        if (!App.checkingWebService()) {
            sendAlert("Service is unavailable right now. Try again later", Alert.AlertType.WARNING);
            return;
        }

        //Invoking 'save as' dialog and choosing place to save file
        saveAs.setInitialFileName("Hello_world.rtf");
        File helloWorldFile = saveAs.showSaveDialog(stage);

        if (helloWorldFile != null) {
            //User decided there to save file
            try {

                //Trying to download file
                App.getFileWebServiceConnector().downloadFile("/files/Hello_world.rtf", helloWorldFile);
                sendAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
                log.info("File has been downloaded");

                //If download was successful, changing default value of webServiceURL in the property file
                PropertiesManager.setPropertyValue("webServiceURL", serviceURL);
            } catch (PropertiesManagerException | WebServiceConnectorException ex) {
                sendAlert(ex.getMessage(), Alert.AlertType.ERROR);
                log.error(ex.getMessage(), ex);
            }
        } else {
            //User canceled 'save as' dialog
            log.info("File downloading was canceled by user");
        }
    }
}