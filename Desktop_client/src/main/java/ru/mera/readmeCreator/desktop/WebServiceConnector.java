/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class which represents web service connection in this app.
 * It's responsible for sending Http requests to web service, establishing connection with it.
 * User of the class can communicate with web service via provided public methods.
 * Sending of various Http requests is hidden from the user.
 * To call this class's constructor user must firstly verify passed webServiceUrl.
 **/
public class WebServiceConnector {
    private final URL webService;
    private HttpURLConnection connection;
    private final Logger log = LoggerFactory.getLogger(WebServiceConnector.class);

    /**
     * Constructs new WebServiceConnector. It is assumed that webServiceUrl is correct URL
     * @param webServiceUrl correct web service url
     */
    WebServiceConnector(URL webServiceUrl) {
        this.webService = webServiceUrl;
    }

    /**
     * Gets file from web service
     * @param mapping service mapping for 'GET' request
     * @param saveToFile local file in which the file from the service is saved
     * @throws WebServiceConnectorException some exceptions occurred during downloading of the file
     */
    public void downloadFile(String mapping, File saveToFile) throws WebServiceConnectorException {
        //Constructing full URL to which 'GET' request will be sent
        URL fullURL;
        try {
            fullURL = new URL(webService.toString() + mapping);
        } catch (IOException ex) {
            throw new WebServiceConnectorException("Can't generate full URL", ex);
        }

        //Reading response code and validating it
        int responseCode = sendGetRequest(fullURL);
        log.info("Response code from the server: {}", responseCode);
        if (responseCode >= 400) {
            throw new WebServiceConnectorException("Bad response code: " + responseCode);
        }

        //If response code is ok, reading response and closing connection
        readResponse(saveToFile);
        connection.disconnect();
    }

    //Sends 'GET' request to the web service and returns response code
    private int sendGetRequest(URL url) throws WebServiceConnectorException {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "desktop");

            log.info("Sending 'GET' request to URL: {}", url);
            return connection.getResponseCode();
        } catch (IOException ex) {
            connection.disconnect();
            throw new WebServiceConnectorException("There is a problem with connection to the server", ex);
        }
    }

    //Reads response from service
    private void readResponse(File helloWorldFile) throws WebServiceConnectorException {
        String inputLine;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             FileWriter out = new FileWriter(helloWorldFile)) {

            inputLine = in.readLine();
            while (inputLine != null) {
                out.write(inputLine + "\n");
                inputLine = in.readLine();
            }
            out.flush();
        } catch (IOException ex) {
            connection.disconnect();
            throw new WebServiceConnectorException("There is a problem with reading the response", ex);
        }
    }

    /**
     * Compares this web service's url with another url from the string
     * @param url url to compare with
     * @return true if both urls are equal and false otherwise
     */
    public boolean isUrlEqual(String url) {
        return webService.toString().equals(url);
    }
}