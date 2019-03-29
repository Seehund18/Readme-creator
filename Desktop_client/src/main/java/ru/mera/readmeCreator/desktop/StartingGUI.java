package ru.mera.readmeCreator.desktop;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URL;

public class StartingGUI extends Application {
    private ServerConnector serverConnector;

    @Override
    public void init() throws Exception {
        serverConnector = new ServerConnector(new URL("http://localhost:8080"));
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
                serverConnector.sendGetRequest("/files/HelloWorld.rtf");
                Alert notify = new Alert(Alert.AlertType.INFORMATION, "Your file has been downloaded to this project path", ButtonType.OK);
                notify.showAndWait();
            } catch (ServerConnectorException ex) {
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
