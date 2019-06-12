package ru.mera.readme_creator.web_client.controllers;

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
    private String jiraId;
    private String jiraDescrip;

    /**
     * File path for submit button type
     */
    private String filePath;

    public String getJiraId() {
        return jiraId;
    }

    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public String getJiraDescrip() {
        return jiraDescrip;
    }

    public void setJiraDescrip(String jiraDescrip) {
        this.jiraDescrip = jiraDescrip;
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