package ru.mera.readmeCreator.web_client;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.io.IOException;


@ManagedBean(eager = true)
public class WebServiceConnector {

    @ManagedProperty(value = "#{fileGeneratorWebService}")
    private WebService webService;

    public WebService getWebService() {
        return webService;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public void getFile() throws IOException {
        System.out.println("I'm here");
        FacesContext.getCurrentInstance().getExternalContext().dispatch("error.xhtml");
//        if(webService.isAvailable()) {
//            String uri = "http://localhost:8080/files/Hello_world.rtf";
//            FacesContext.getCurrentInstance().getExternalContext().redirect(uri);
//        } else {
//
//        }
    }
}