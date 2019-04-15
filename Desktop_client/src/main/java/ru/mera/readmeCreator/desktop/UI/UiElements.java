/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.UI;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import ru.mera.readmeCreator.desktop.PropertiesManager;

import java.io.File;

/**
 * Ui elements of the app's main window
 */
public class UiElements {
    static Label webServiceLabel = new Label("Please, enter a web service URI.\n"
            + "Examples: http://localhost:8080, http://myService.com");
    static TextField webServiceUrlField = new TextField();
    static Text urlStatus = new Text();
    static Label helloLabel = new Label("Press to generate Hello World.rtf file:");
    static Button generateButton = new Button("Generate HelloWorld file");
    static Label textToFileLabel = new Label("Enter text which will be written to file:");
    static TextArea textToFile = new TextArea();
    static Button submitButton = new Button("Submit");
    static FileChooser saveAs = new FileChooser();
    static Line[] separatLines = new Line[2];
    static {
        for (int i = 0; i < separatLines.length; i++) {
            separatLines[i] = new Line();
        }
    }

    /**
     * Initialization of UI elements. Sets basic text, size, font and configuration
     */
    static void config() {
        webServiceLabel.setFont(new Font(14));
        webServiceLabel.setPrefWidth(350);
        webServiceLabel.setTextAlignment(TextAlignment.CENTER);
        webServiceLabel.setWrapText(true);
        webServiceLabel.setLabelFor(webServiceUrlField);

        webServiceUrlField.setFont(new Font(13));
        webServiceUrlField.setPromptText("Enter URI of web service...");
        webServiceUrlField.setText(PropertiesManager.getPropertyValue("webServiceURL"));
        webServiceUrlField.setMaxSize(200,10);

        urlStatus.setText("Valid URL");
        urlStatus.setFill(Color.GREEN);

        helloLabel.setFont(new Font(14));

        textToFileLabel.setFont(new Font(14));
        textToFileLabel.setTextAlignment(TextAlignment.CENTER);
        textToFileLabel.setWrapText(true);
        textToFileLabel.setLabelFor(textToFile);

        textToFile.setMaxSize(400, 100);
        textToFile.setWrapText(true);

        saveAs.setTitle("Save file as");
        saveAs.setInitialFileName("Hello World.rtf");
        saveAs.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("RTF", "*.rtf")
        );
        saveAs.setInitialDirectory(new File(System.getProperty("user.home")));

        for (Line line : separatLines ) {
            line.setEndX(line.getScene().getWidth());
        }
    }
}
