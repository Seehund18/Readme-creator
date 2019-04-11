package ru.mera.readmeCreator.web_client;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Interface for web services in this program.
 * For now, classes which extends this abstract class,
 * must define when they are available (isAvailable() method)
 * and must be able to sendGetRequests (sendGetRequest() method)
 */
public abstract class WebService {
    //URL of web service
    protected URL webServiceURL;

    protected URL getWebServiceURL() {
        return this.webServiceURL;
    }

    protected void setWebServiceURL(String webServiceURL) throws MalformedURLException {
        this.webServiceURL = new URL(webServiceURL);
    }

    /**
     * Defines when this web service is available. For this, sending 'GET' request
     * directly to service URL
     * @return true - service is available and false otherwise
     */
    public boolean isAvailable() {
        return sendGetRequest("") != -1;
    }

    /**
     * Method for sending 'GET' requests to web service
     * @param getMapping mapping for 'GET' request
     * @return response code or -1 if connection to server was refused
     */
    public abstract int sendGetRequest(String getMapping);
}
