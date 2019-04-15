/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.UI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readmeCreator.desktop.*;

import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.net.MalformedURLException;

import static ru.mera.readmeCreator.desktop.UI.UiElements.*;

public class SubmitButtonHandler implements EventHandler<ActionEvent>, AlertSender {
    private Stage stage;
    private static Logger log = LoggerFactory.getLogger(SubmitButtonHandler.class);

    SubmitButtonHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        log.info("{\"info\":\"" + textToFile.getText()+"\"}");
        //TODO можно как-то убрать повторяющиеся блоки
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
        File dummyFile = saveAs.showSaveDialog(stage);

        //If user choose where to save file, then try to download file from service
        if (dummyFile != null) {
            try {
                //Trying to download file
                ObjectMapper mapper = new ObjectMapper();
                UserData userData = new UserData(textToFile.getText());
                String jsonString = mapper.writeValueAsString(userData);
                log.info(jsonString);
                App.getFileWebServiceConnector().downloadFile("/files/Readme.rtf", jsonString, dummyFile);
                sendAlert("Your file has been downloaded", Alert.AlertType.INFORMATION);
                log.info("File has been downloaded");

                //If download was successful, changing default value of webServiceURL in property file
                PropertiesManager.setPropertyValue("webServiceURL", serviceURL);
            } catch (PropertiesManagerException | WebServiceConnectorException ex) {
                sendAlert(ex.getMessage(), Alert.AlertType.ERROR);
                log.error(ex.getMessage(), ex);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            //User canceled 'save as' dialog
            log.info("File downloading was canceled by user");
        }


    }
}
