package ru.mera.readmeCreator.desktop;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
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

        Button generateButton = new Button("Generate file");
        generateButton.setPrefSize(100, 100);
        generateButton.setOnAction(event -> serverConnector.sendGetRequest("/getFile"));

        FlowPane root = new FlowPane(generateButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Readme generator");

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
