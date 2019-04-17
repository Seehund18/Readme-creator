package ru.mera.readmeCreator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

/**
 * Controller of this app
 */
@ManagedBean(eager = true)
@SessionScoped
public class MainController implements Serializable {
    /**
     * Url entered in webServiceURL
     */
    private String url;
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

    public void setError(Error error) {
        log.debug("Setting error object\n");
        this.error = error;
    }

    /**
     * After constructing of this object, sets url field to value from URL cookie
     */
    @PostConstruct
    private void init() {
        url = CookieHelper.getCookieValue("URL");
    }

    /**
     * Gets "Hello_World.rtf" file from the web service
     * @throws IOException can't generate url for web service or problems with redirecting
     * @throws WebServiceConnectorException problem in WebServiceConnector
     */
    public void getHelloFile() throws IOException, WebServiceConnectorException {
        //Getting url from the field and setting web service
        connector.setWebService(url);

        if (connector.isServiceAvailable()) {
            //Adding cookie which lives for 2 days
            CookieHelper.addPermanentCookie("URL", url, 172_800);
            log.info("Service is available. Redirecting user...\n");
            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/files/Hello_world.rtf");
            log.info("User was redirected\n");
        } else {
            log.info("Service is unavailable\n");
            error.setMessage("Service is unavailable. Try again later");
            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
        }
    }

    /**
     * Gets "User_data.rtf" file from the web service
     * @throws IOException can't generate url for web service or problems with redirecting
     * @throws WebServiceConnectorException problem in WebServiceConnector
     */
    public void getUserDataFile() throws IOException, WebServiceConnectorException {
        //Getting url from the field and setting web service
        connector.setWebService(url);

        if (connector.isServiceAvailable()) {
            //Adding cookie which lives for 2 days
            CookieHelper.addPermanentCookie("URL", url, 172_800);

            //Sending POST request to service
            log.info("Service is available. Sending POST request to {}", url + "/files/User_data.rtf");
            connector.sendPostRequest("/files/User_data.rtf", userData.toString());

            log.info("Redirecting user to {}", url + "/files/User_data.rtf");
            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/files/User_data.rtf");
            log.info("User was redirected\n");
        } else {
            log.info("Service is unavailable\n");
            error.setMessage("Service is unavailable. Try again later");
            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
        }
    }
}