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
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implementation of WebService Connector.
 * Represents connection to file generator service.
 * It's responsible for sending Http requests to web service, establishing connection with it.
 **/
public class FileWebServiceConnector extends WebServiceConnector {
    private final Logger log = LoggerFactory.getLogger(FileWebServiceConnector.class);

    /**
     * Constructs new FileWebServiceConnector. It is assumed that webServiceUrl is correct URL
     * @param webServiceUrl correct web service url
     */
    FileWebServiceConnector(URL webServiceUrl) {
        this.webService = webServiceUrl;
    }

    /**
     * Gets file from web service
     * @param mapping service mapping for 'GET' request
     * @param saveToFile local file in which the file from the service is saved
     * @throws WebServiceConnectorException some exceptions occurred during downloading of the file
     */
    public void downloadFile(String mapping, File saveToFile) throws WebServiceConnectorException {
        //Sending and reading response code and validating it
        int responseCode = sendGetRequest(mapping);
        log.info("Response code from the server: {}", responseCode);
        if (responseCode >= 400) {
            throw new WebServiceConnectorException("Bad response code: " + responseCode);
        }

        //If response code is ok, reading response and closing connection
        readResponseToFile(saveToFile);
        connection.disconnect();
    }

    @Override
    protected int sendGetRequest(String mapping) throws WebServiceConnectorException {
        URL fullURL;
        try {
            fullURL = new URL(webService.toString() + mapping);
        } catch (IOException ex) {
            throw new WebServiceConnectorException("Can't generate full URL", ex);
        }

        try {
            log.info("Sending 'GET' request to URL: {}", fullURL);
            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "desktop");

            int responseCode;
            try {
                responseCode = connection.getResponseCode();
            } catch (ConnectException ex) {
                log.debug("ConnectException was caught");
                return -1;
            }
            log.info("Response code: {}", responseCode);
            return responseCode;
        } catch (IOException ex) {
            connection.disconnect();
            throw new WebServiceConnectorException("There is a problem with connection to the server", ex);
        }
    }

    /**
     * Reads response from service
     * @param saveToFile file to which save the response
     * @throws WebServiceConnectorException problem with reading the response
     */
    private void readResponseToFile(File saveToFile) throws WebServiceConnectorException {
        String inputLine;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             FileWriter out = new FileWriter(saveToFile)) {

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
}