
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaFx extends Application{

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();

    }

    private void sendHttpRequest() {
        //TODO: Refactor all that
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("http://localhost:8080/getFile");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "desktop");

            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            File helloWorldFile = new File("Hello World.rtf");
            System.out.println(helloWorldFile.exists());

            String inputLine;
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                FileWriter out = new FileWriter(helloWorldFile)) {
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    out.write(inputLine + "\n");
                    response.append(inputLine);
                }
                out.flush();
            }
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public void start(Stage stage) {
        stage.setHeight(500);
        stage.setWidth(500);

        Button generateButton = new Button("Generate file");
        generateButton.setAlignment(Pos.CENTER);
        generateButton.setOnAction(event -> sendHttpRequest());

        Group root = new Group(generateButton);
        root.setAutoSizeChildren(true);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Readme generator");

        stage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
