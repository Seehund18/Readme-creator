package ru.mera.readme_creator.web_client.webService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
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
        } finally {
            fileWebService.disconnect();
        }
    }

    /**
     * Sends info to service, validates the response code and redirects user to file URL for downloading
     * @param fileName  name of the file to create
     * @param info info for the file
     * @throws IOException exception while redirecting user
     * @throws WebServiceException exception from web service
     */
    public static void downloadFile(String fileName, String info) throws IOException, WebServiceException {
        String fullFileName = "/files/" + fileName;
        int responseCode = fileWebService.sendPostRequest(fullFileName, info);
        if (responseCode > HttpServletResponse.SC_BAD_REQUEST) {
            //If responseCode is an error code (bigger or equal than 400)
            fileWebService.disconnect();
            throw new WebServiceException("Bad response code: " + responseCode);
        }
        fileWebService.disconnect();

        //Redirecting user to a file
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect(fileWebService.getUrl() + "/files/" + fileName);

    }
}