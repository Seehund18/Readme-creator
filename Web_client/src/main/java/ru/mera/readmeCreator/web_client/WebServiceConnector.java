package ru.mera.readmeCreator.web_client;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.IOException;


@ManagedBean(eager = true)
public class WebServiceConnector {

    @ManagedProperty(value = "#{fileGeneratorWebService}")
    private WebService webService;

    @ManagedProperty(value = "#{userData}")
    private UserData userData;

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public WebService getWebService() {
        return webService;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public void getFile() throws IOException {
        if(webService.isAvailable()) {
            String url = userData.getUrl() + "/files/Hello_world.rtf";
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } else {
            FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
        }
    }
}