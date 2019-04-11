package ru.mera.readmeCreator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean(eager = true)
public class WebServiceConnector {
    private final Logger log = LoggerFactory.getLogger(WebServiceConnector.class);

    @ManagedProperty(value = "#{fileGeneratorWebService}")
    private WebService webService;

    @ManagedProperty(value = "#{userData}")
    private UserData userData;

    public void setUserData(UserData userData) {
        log.info("Setting UserData object\n");
        this.userData = userData;
    }

    public void setWebService(WebService webService) {
        log.info("Setting webService object\n");
        this.webService = webService;
    }

    public void getFile() throws IOException {
        log.info("User pushed button");
        webService.setWebServiceURL(userData.getUrl());
        if(webService.isAvailable()) {
            log.info("Service is available. Redirecting user...\n");
            String url = userData.getUrl() + "/files/Hello_world.rtf";
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            log.info("User was redirected\n");
        } else {
            log.info("Service is unavailable\n");
            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
        }
    }
}