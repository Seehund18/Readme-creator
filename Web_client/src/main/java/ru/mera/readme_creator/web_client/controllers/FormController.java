package ru.mera.readme_creator.web_client.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readme_creator.web_client.CookieHelper;
import ru.mera.readme_creator.web_client.Error;
import ru.mera.readme_creator.web_client.JiraPair;
import ru.mera.readme_creator.web_client.UserData;
import ru.mera.readme_creator.web_client.webService.WebServiceException;
import ru.mera.readme_creator.web_client.webService.WebServiceManager;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Controller for form.xhtml
 */
@ManagedBean(eager = true)
@SessionScoped
public class FormController implements Serializable {
    private final Logger log = LoggerFactory.getLogger(FormController.class);

    @ManagedProperty(value = "#{userData}")
    private UserData userData;

    @ManagedProperty(value = "#{error}")
    private Error error;

    @ManagedProperty(value="#{popupDialogController}")
    private PopupDialogController popupDialogController;

    /**
     * Index of edited jiraPair in jiraList of UserData.
     * It is saved when user pushed 'Edit' button in the table
     */
    private int editIndex;

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
    public UserData getUserData() {
        return userData;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public PopupDialogController getPopupDialogController() {
        return popupDialogController;
    }
    public void setPopupDialogController(PopupDialogController popupDialogController) {
        this.popupDialogController = popupDialogController;
    }

    /**
     * Handler for 'AddJira' ('+') button. Adds jiraPair to jiraPairList of userData
     */
    public void addJira() {
        ArrayList<JiraPair> jiraList = userData.getJiraList();
        JiraPair newPair = new JiraPair(popupDialogController.getJiraId(),
                                        popupDialogController.getJiraDescrip());

        if (jiraList.contains(newPair)) {
            //List already have jiraPair with such jira id
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                "",
                                                "Such id already exists");
            FacesContext.getCurrentInstance().addMessage("popupForm:jiraIdField", msg);
            return;
        }
        log.info("Adding new jiraPair to the table. {}\n", newPair);
        jiraList.add(newPair);
    }

    /**
     * Handler for 'Delete' button in jiraTable. Deletes jiraPair from jiraPairList of userInputData
     * @param pair chosen jira pair in jiraTable
     */
    public void deleteJira(JiraPair pair) {
        log.info("Deleting jira pair from the table.\n {}\n", pair);
        userData.getJiraList().remove(pair);
    }

    /**
     * Handler for 'Edit' button in jiraTable. Set jira id and jira description of popup dialog to chosen jira pair.
     * @param pair chosen jira pair in jiraTable
     */
    public void setDialogJira(JiraPair pair) {
        popupDialogController.setJiraId(pair.getJiraId());
        popupDialogController.setJiraDescrip(pair.getJiraDescrip());
        editIndex = userData.getJiraList().indexOf(pair);
    }

    /**
     * Handler for 'Edit' button in popup dialog. Set jiraPair chosen by user to a new one
     */
    public void editJira() {
        ArrayList<JiraPair> jiraPairList = userData.getJiraList();
        JiraPair oldPair = jiraPairList.get(editIndex);
        JiraPair newPair = new JiraPair(popupDialogController.getJiraId(),
                                        popupDialogController.getJiraDescrip());

        if (!oldPair.equals(newPair) && jiraPairList.contains(newPair) ) {
            //Jira id, entered by user, already exists in the jiraTable
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                "",
                                                "Such jira already exists");
            FacesContext.getCurrentInstance().addMessage("popupForm:jiraIdField", msg);
            return;
        }
        log.info("Editing jira pair.\n Old value: {}.\n New value: {}\n", oldPair, newPair);
        jiraPairList.set(editIndex, newPair);
    }

    /**
     * Handler for submit button
     */
    public void onSubmit() throws IOException, WebServiceException {
        //Checking if is jiraTable is empty
        if (userData.getJiraList().isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                "",
                                                "Jira table is empty");
            FacesContext.getCurrentInstance().addMessage("form:jiraTable", msg);
            return;
        }

        //Setting new URL
        URL serviceUrl;
        try {
            serviceUrl = new URL(userData.getUrl());
        } catch (MalformedURLException ex) {
            log.error("Can't create URL of web service\n", ex);
            return;
        }
        WebServiceManager.setService(serviceUrl);

        String fileName = userData.getParamMap().get("updateId");
        // TODO Поменять захардкоженный User_data.rtf на fileName после того как будет сделан сервис
        if (WebServiceManager.checkWebService()) {
            CookieHelper.addPermanentCookie("URL", serviceUrl.toString(), 172_800);
            log.info("Service is available. Trying to download file\n");
            WebServiceManager.downloadFile("User_data.rtf", userData.toString());
        } else {
            log.info("Service is unavailable\n");
            error.setMessage("Service is unavailable. Try again later");
            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
        }
    }
}