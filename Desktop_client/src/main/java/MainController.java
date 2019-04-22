/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ru.mera.readmeCreator.desktop.JiraInputDialog;
import ru.mera.readmeCreator.desktop.JiraPair;
import ru.mera.readmeCreator.desktop.PropertiesManager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField webServiceUrl;

    @FXML
    private Text urlStatus;

    @FXML
    private GridPane form = new GridPane();
    private ObservableMap<String, TextField> formElements = FXCollections.observableHashMap();
    private ObservableMap<String, Text> formElemStatuses = FXCollections.observableHashMap();

    @FXML
    private TableView<JiraPair> jiraTable = new TableView<>();
    private ObservableList<JiraPair> jiras  = FXCollections.observableArrayList();

    private JiraInputDialog dialog = new JiraInputDialog();

    {
        formElements.put("patchName", new TextField());
        formElements.put("date", new TextField());
        formElements.put("updateId", new TextField());
        formElements.put("releaseVer", new TextField());

        formElemStatuses.put("patchNameStatus", new Text());
        formElemStatuses.put("dateStatus", new Text());
        formElemStatuses.put("updateIdStatus", new Text());
        formElemStatuses.put("releaseVerStatus", new Text());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webServiceUrl.setText(PropertiesManager.getPropertyValue("webServiceURL"));

        formElemStatuses.values()
                .forEach(statusText -> {
                    statusText.setText("It's alright");
                    statusText.setFill(Color.GREEN);
                });

        form.addColumn(1, formElements.values().toArray(new Node[0]));
        form.addColumn(2, formElemStatuses.values().toArray(new Node[0]));

        TableColumn<JiraPair, String> jiraIdColumn = new TableColumn<>("Jira ID");
        TableColumn<JiraPair, String> jiraDescripColumn = new TableColumn<>("Jira description");
        jiraIdColumn.setCellValueFactory(new PropertyValueFactory<>("jiraId"));
        jiraDescripColumn.setCellValueFactory(new PropertyValueFactory<>("jiraDescrip"));
        jiraTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        jiraTable.getColumns().addAll(jiraIdColumn, jiraDescripColumn);
        jiraTable.setItems(jiras);
    }

    @FXML
    public void addButton(Event e) {
//        ru.mera.readmeCreator.desktop.JiraInputDialog dialog = new ru.mera.readmeCreator.desktop.JiraInputDialog();
        Optional<JiraPair> jiraPair = dialog.showAndWait();
        jiraPair.ifPresent(pair -> jiras.add(pair));
    }
}
