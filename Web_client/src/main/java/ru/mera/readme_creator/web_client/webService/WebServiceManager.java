package ru.mera.readme_creator.web_client.webService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.net.URL;

/**
 * Manager of web service. All interactions with service are done via this class.
 */
public class WebServiceManager {
    private static final Logger log = LoggerFactory.getLogger(WebServiceManager.class);

    /**
     * File connector to the web service
     */
    private static FileWebService fileWebService;

    /**
     * Sets connector. If fileWebService is not instantiated yet,
     * creates new connector. Otherwise sets new URL in existed connector
     * @param serviceURL url of web service
     */
    public static void setService(URL serviceURL) {
        if (fileWebService == null) {
            fileWebService = new FileWebService(serviceURL);
        } else {
            fileWebService.setUrl(serviceURL);
        }
    }

    public static String getServiceUrl() {
        return fileWebService.getUrl().toString();
    }

    /**
     * Checks fileWebService availability
     * @return true if service is available and false otherwise
     */
    public static boolean checkWebService() {
        try {
            return fileWebService.isServiceAvailable();
        } catch (WebServiceException ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }


    public static void downloadFile(String fileName, String info) throws IOException, WebServiceException {
        fileWebService.sendPostRequest("/files/User_data.rtf", info);
        fileWebService.disconnect();
        FacesContext.getCurrentInstance().getExternalContext().redirect(fileWebService.getUrl() + "/files/" + fileName);

    }
}