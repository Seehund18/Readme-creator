package ru.mera.readmeCreator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void setUserData(UserData userData) {
        log.info("Setting UserData object\n");
        this.userData = userData;
    }

    public void setConnector(WebServiceConnector webServiceConnector) {
        log.info("Setting connector object\n");
        this.connector = webServiceConnector;
    }

    public void setError(Error error) {
        log.info("Setting error object\n");
        this.error = error;
    }

    /**
     * Gets helloWorld file from web service
     * @throws IOException can't generate url for web service or problems with redirecting
     * @throws WebServiceConnectorException problem in WebServiceConnector
     */
    public void getHelloFile() throws IOException, WebServiceConnectorException {
        log.info("User pushed the button\n");
        String url = userData.getUrl();
        connector.setWebService(url);
        if(connector.isServiceAvailable()) {
            CookieHelper.addCookie("URL", url);
            log.info("Service is available. Redirecting user...\n");
            FacesContext.getCurrentInstance().getExternalContext().redirect(url + "/files/Hello_world.rtf");
            log.info("User was redirected\n");
        } else {
            log.info("Service is unavailable\n");
            error.setMessage("Service is unavailable. Try again later");
            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
        }
    }
}