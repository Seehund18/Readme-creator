/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

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

    public URL getWebService() {
        return this.webService;
    }

    public void setWebService(String webService) throws MalformedURLException {
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
    protected abstract int sendGetRequest(String getMapping) throws WebServiceConnectorException;

    /**
     * Method for sending 'POST' requests to web service
     */
    protected abstract int sendPostRequest(String postMapping, String info) throws WebServiceConnectorException;
}
