package ru.mera.readmeCreator.web_client;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Interface for web services in this program.
 */
public abstract class WebServiceConnector {
    //URL of web service
    protected URL webService;
    //Connection to the web service
    protected HttpURLConnection connection;

    protected URL getWebService() {
        return this.webService;
    }

    protected void setWebService(String webService) throws MalformedURLException {
        this.webService = new URL(webService);
    }

    /**
     * Defines when this web service is available. For this, sending 'GET' request
     * directly to service URL
     * @return true - service is available and false otherwise
     * @throws WebServiceConnectorException problems with sending request
     */
    public boolean isServiceAvailable() throws WebServiceConnectorException {
        return sendGetRequest("") != -1;
    }

    /**
     * Method for sending 'GET' requests to web service
     * @param getMapping mapping for 'GET' request
     * @return response code or -1 if connection to server was refused
     * @throws WebServiceConnectorException problems with sending request
     */
    public abstract int sendGetRequest(String getMapping) throws WebServiceConnectorException;
}
