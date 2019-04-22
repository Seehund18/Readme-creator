/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.mera.readmeCreator.desktop.PropertiesManager;
import ru.mera.readmeCreator.desktop.PropertiesManagerException;

public class MainApp extends Application {

    /**
     * Initialization of propertiesManager
     */
    private void propertiesInit() {
        try {
            PropertiesManager.init();
        } catch (PropertiesManagerException ex) {
            //If there is an exception, alerting user and exit the program
//            log.error(ex.getMessage(), ex);
//            sendAlert(ex.getMessage(), Alert.AlertType.ERROR);
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
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
//        setStageAtCenter(stage);

//        UiElements.config();
    }
}
