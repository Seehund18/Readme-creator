package ru.mera.readmeCreator.desktop;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.File;
import java.net.URL;

public class App extends Application {
    private WebServiceConnector webServiceConnector;

    @Override
    public void init() throws Exception {
        webServiceConnector = new WebServiceConnector(new URL("http://localhost:8080"));
    }

    @Override
    public void start(Stage stage) {
        stage.setHeight(500);
        stage.setWidth(500);
        stage.setTitle("Readme generator");

        Button generateButton = new Button("Generate file");
        generateButton.setPrefSize(100, 100);
        generateButton.setOnAction(event -> {
            try {
                FileChooser saveAs = new FileChooser();
                saveAs.setTitle("Save file as");
                saveAs.setInitialFileName("Hello World.rtf");

                File helloWorldFile = saveAs.showSaveDialog(stage);
                if (helloWorldFile != null) {
                    webServiceConnector.sendGetRequest("/files/HelloWorld.rtf", helloWorldFile);
                    Alert notify = new Alert(Alert.AlertType.INFORMATION, "Your file has been downloaded", ButtonType.OK);
                    notify.showAndWait();
                } else {
                    Alert wrongFileName = new Alert(Alert.AlertType.WARNING, "Please, specify a file name", ButtonType.OK);
                    wrongFileName.showAndWait();
                }
            } catch (WebServiceConnectorException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                error.showAndWait();
            }
        });

        BorderPane root = new BorderPane(generateButton);
        root.setCenter(generateButton);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
        setStageAtCenter(stage);
    }

    private void setStageAtCenter(Stage stage) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
