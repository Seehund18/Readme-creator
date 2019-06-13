package ru.mera.readme_creator.web_client.controllers;

import ru.mera.readme_creator.web_client.JiraPair;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Controller of popup dialog window. The window is the same for 'add jira' dialog and 'edit jira' dialog.
 * The difference between those two dialogs is a submit button.
 * Add submit button defined in addButton.xhtml.
 * Edit submit button defined in editButton.xhtml.
 */
@Named
@SessionScoped
public class PopupDialogController implements Serializable {

    /**
     * Jira pair, entered by user
     */
    private JiraPair userJiraPair = new JiraPair();

    /**
     * File path for submit button type
     */
    private String filePath;

    public JiraPair getUserJiraPair() {
        return userJiraPair;
    }
    public void setUserJiraPair(JiraPair userJiraPair) {
        this.userJiraPair = userJiraPair;
    }

    public String getFilePath() {
        return filePath;
    }

    /**
     * Setting file path for button to addButton
     */
    public void setAddDialogType() {
        filePath = "/xhtml/buttons/addButton.xhtml";
    }

    /**
     * Setting file path for button to editButton
     */
    public void setEditDialogType() {
        filePath = "/xhtml/buttons/editButton.xhtml";
    }
}