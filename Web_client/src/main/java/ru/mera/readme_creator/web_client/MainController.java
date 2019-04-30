package ru.mera.readme_creator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller of this app
 */
@ManagedBean(eager = true)
@SessionScoped
public class MainController implements Serializable {
    private final Logger log = LoggerFactory.getLogger(MainController.class);

    //Injecting FileWebServiceConnector into this field
    @ManagedProperty(value = "#{fileWebServiceConnector}")
    private WebServiceConnector connector;

    //Injecting UserData into this field
    @ManagedProperty(value = "#{userData}")
    private UserData userData;

    //Injecting Error into this field
    @ManagedProperty(value = "#{error}")
    private Error error;

    /**
     * Url entered in webServiceURL
     */
    private String url;

    private Map<String, String> parametersMap = new HashMap<>();

    private ArrayList<JiraPair> jiraPairs = new ArrayList<>();

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setConnector(WebServiceConnector webServiceConnector) {
        log.debug("Setting connector object\n");
        this.connector = webServiceConnector;
    }

    public void setUserData(UserData userData) {
        log.debug("Setting UserData object\n");
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Map<String, String> getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(Map<String, String> parametersMap) {
        this.parametersMap = parametersMap;
    }

    public ArrayList<JiraPair> getJiraPairs() {
        return jiraPairs;
    }


    /**
     * After constructing of this object, sets url field to value from URL cookie
     */
    @PostConstruct
    private void init() {
        url = CookieHelper.getCookieValue("URL");

        parametersMap.put("patchName", "");
        parametersMap.put("date", "");
        parametersMap.put("updateId", "");
        parametersMap.put("releaseVersion", "");

        jiraPairs.add(new JiraPair("bgdb", "ghkj"));
        jiraPairs.add(new JiraPair("lolol", "kjhlkjh"));
    }

    public void addJira() {
        JiraPair jira = new JiraPair(newJiraId, newJiraDescrip);
        if (jiraPairs.contains(jira)) {
            log.info("Jira existed");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Jira exists",
                    "Such jira id already exists");
            FacesContext
                    .getCurrentInstance()
                    .addMessage("popupForm:JiraId", msg);
            return;
        }
        log.info("Adding new jira: {}", jira);
        jiraPairs.add(jira);
    }

    public void deleteJira(JiraPair pair) {
        jiraPairs.remove(pair);
    }



    public void editJira(JiraPair pair) {
        int index = jiraPairs.indexOf(pair);

        newJiraId = pair.getJiraId();
        newJiraDescrip = pair.getJiraDescrip();



    }

//    /**
//     * Gets "Hello_World.rtf" file from the web service
//     * @throws IOException can't generate url for web service or problems with redirecting
//     * @throws WebServiceConnectorException problem in WebServiceConnector
//     */
//    public void downHelloFile() throws IOException, WebServiceConnectorException {
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
//     * @throws WebServiceConnectorException problem in WebServiceConnector
//     */
//    public void downUserDataFile() throws IOException, WebServiceConnectorException {
//        //Getting url from the field and setting web service
//        connector.setWebService(url);
//
//        if (connector.isServiceAvailable()) {
//            //Adding cookie which lives for 2 days
//            CookieHelper.addPermanentCookie("URL", url, 172_800);
//
//            //Sending POST request to service
//            log.info("Service is available. Sending POST request to {}", url + "/files/User_data.rtf");
//            connector.sendPostRequest("/files/User_data.rtf", userData.toString());
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