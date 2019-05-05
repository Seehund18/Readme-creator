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
 * Controller of this app
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
     * Index of edited jiraPair in jiraPairsList of UserData. It is saved when user pushed 'Edit' button
     */
    private int editIndex;

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setError(Error error) {
        log.debug("Setting error object\n");
        this.error = error;
    }

    public PopupDialogController getPopupDialogController() {
        return popupDialogController;
    }

    public void setPopupDialogController(PopupDialogController popupDialogController) {
        this.popupDialogController = popupDialogController;
    }

    /**
     * Handler for 'AddJira' ('+') button. Adds jiraPair to jiraPairList of userInputData
     */
    public void addJira() {
        ArrayList<JiraPair> jiraPairList = userData.getJiraPairList();
        JiraPair newPair = new JiraPair(popupDialogController.getJiraId(),
                                        popupDialogController.getJiraDescrip());

        if (jiraPairList.contains(newPair)) {
            //List already have jiraPair with such jira id
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                "Such id already exists",
                                                "Such id already exists");
            FacesContext.getCurrentInstance().addMessage("popupForm:jiraIdField", msg);
            return;
        }
        log.info("Adding new jiraPair to the table. {}", newPair);
        jiraPairList.add(newPair);
    }

    /**
     * Handler for 'Delete' button in jiraTable. Deletes jiraPair from jiraPairList of userInputData
     * @param pair chosen jira pair in jiraTable
     */
    public void deleteJira(JiraPair pair) {
        log.info("Deleting jira pair from the table.\n {}", pair);
        userData.getJiraPairList().remove(pair);
    }

    /**
     * Handler for 'Edit' button in jiraTable. Set jira id and jira description of popup dialog to chosen jira pair.
     * @param pair chosen jira pair in jiraTable
     */
    public void setDialogJira(JiraPair pair) {
        popupDialogController.setJiraId(pair.getJiraId());
        popupDialogController.setJiraDescrip(pair.getJiraDescrip());
        editIndex = userData.getJiraPairList().indexOf(pair);
    }

    /**
     * Handler for 'Edit' button in popup dialog. Set jiraPair chosen by user to a new one
     */
    public void editJira() {
        ArrayList<JiraPair> jiraPairList = userData.getJiraPairList();
        JiraPair oldPair = jiraPairList.get(editIndex);
        JiraPair newPair = new JiraPair(popupDialogController.getJiraId(),
                                        popupDialogController.getJiraDescrip());

        if (!oldPair.equals(newPair) && jiraPairList.contains(newPair) ) {
            //Jira id, entered by user, already exists in the jiraTable
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                "",
                                                "Such jira already exists");
            FacesContext.getCurrentInstance().addMessage("form:jiraTable", msg);
            return;
        }
        log.info("Editing jira pair.\n Old value: {}.\n New value: {}", oldPair, newPair);
        jiraPairList.set(editIndex, newPair);
    }

    /**
     * Handler for submit button
     */
    public void onSubmit() throws IOException, WebServiceException {
        if (userData.getJiraPairList().isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                "",
                                                "Jira table is empty");
            FacesContext.getCurrentInstance().addMessage("form:jiraTable", msg);
            return;
        }
        log.info("Parameters: {}", userData);

        URL serviceUrl;
        try {
            serviceUrl = new URL(userData.getUrl());
        } catch (MalformedURLException ex) {
            log.error("Can't create URL of web service", ex);
            return;
        }
        log.info("Service Url: {}", serviceUrl);
        WebServiceManager.setService(serviceUrl);
        log.info("\n File service url: {}\n", WebServiceManager.getServiceUrl());

        if (WebServiceManager.checkWebService()) {
            CookieHelper.addPermanentCookie("URL", serviceUrl.toString(), 172_800);
            log.info("Service is available. Redirecting user...\n");
            WebServiceManager.downloadFile("User_data.rtf", userData.toString());
        } else {
            log.info("Service is unavailable\n");
            error.setMessage("Service is unavailable. Try again later");
            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
        }
    }



//    /**
//     * Gets "Hello_World.rtf" file from the web service
//     * @throws IOException can't generate url for web service or problems with redirecting
//     * @throws WebServiceException problem in WebServiceConnector
//     */
//    public void downHelloFile() throws IOException, WebServiceException {
//        //Getting url from the field and setting web service
//        connector.setWebService(url);
//
//        if (connector.isServiceAvailable()) {
//            //Adding cookie which lives for 2 days
//            CookieHelper.addPermanentCookie("URL", url, 172_800);
//            log.info("Service is available. Redirecting user...\n");
//            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/files/Hello_world.rtf");
//            log.info("User was redirected\n");
//        } else {
//            log.info("Service is unavailable\n");
//            error.setMessage("Service is unavailable. Try again later");
//            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
//        }
//    }
//
//    /**
//     * Gets "User_data.rtf" file from the web service
//     * @throws IOException can't generate url for web service or problems with redirecting
//     * @throws WebServiceException problem in WebServiceConnector
//     */
//    public void downUserDataFile() throws IOException, WebServiceException {
//        //Getting url from the field and setting web service
//        connector.setWebService(url);
//
//        if (connector.isServiceAvailable()) {
//            //Adding cookie which lives for 2 days
//            CookieHelper.addPermanentCookie("URL", url, 172_800);
//
//            //Sending POST request to service
//            log.info("Service is available. Sending POST request to {}", url + "/files/User_data.rtf");
//            connector.sendPostRequest("/files/User_data.rtf", userInputData.toString());
//
//            log.info("Redirecting user to {}", url + "/files/User_data.rtf");
//            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/files/User_data.rtf");
//            log.info("User was redirected\n");
//        } else {
//            log.info("Service is unavailable\n");
//            error.setMessage("Service is unavailable. Try again later");
//            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
//        }
//    }
}