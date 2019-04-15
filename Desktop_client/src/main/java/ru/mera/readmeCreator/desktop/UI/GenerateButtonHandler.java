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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static ru.mera.readmeCreator.desktop.UI.UiElements.saveAs;
import static ru.mera.readmeCreator.desktop.UI.UiElements.webServiceUrlField;

class GenerateButtonHandler implements EventHandler<ActionEvent>, AlertSender {
    private Stage stage;
    private Logger log = LoggerFactory.getLogger(GenerateButtonHandler.class);

    public GenerateButtonHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        //Checking flag
        if (!UrlStatusListener.isUrlValid()) {
            sendAlert("URL is not valid", Alert.AlertType.ERROR);
            return;
        }

        String serviceURL = webServiceUrlField.getText();
        try {
            App.settingConnector(serviceURL);
            if (!App.checkingWebService()) {
                sendAlert("Service is unavailable right now. Try again later", Alert.AlertType.WARNING);
                return;
            }
        } catch (MalformedURLException ex) {
            log.error("Can't create URL of web service", ex);
            sendAlert("Can't create URL of web service", Alert.AlertType.ERROR);
        } catch (WebServiceConnectorException ex) {
            log.error(ex.getMessage(), ex);
            sendAlert(ex.getMessage(), Alert.AlertType.ERROR);
        }

        //Invoking 'save as' dialog and choosing place to save file
        File helloWorldFile = saveAs.showSaveDialog(stage);

        //If user choose where to save file, then try to download file from service
        if (helloWorldFile != null) {
            try {
                //Trying to download file
                App.getFileWebServiceConnector().downloadFile("/files/Hello_world.rtf", helloWorldFile);
                sendAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
                log.info("File has been downloaded");

                //If download was successful, changing default value of webServiceURL in property file
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