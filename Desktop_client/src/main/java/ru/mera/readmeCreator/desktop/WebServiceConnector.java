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
 * Class which represents web service in this app.
 * It's responsible for sending Http requests to web service, establishing connection with it.
 * User of the class can communicate with web service via provided public methods.
 * Sending of various Http requests is hidden from the user.
 **/
public class WebServiceConnector {
    private final URL webService;
    private HttpURLConnection connection;
    private Logger log = LoggerFactory.getLogger(WebServiceConnector.class);

    WebServiceConnector(URL webService) {
        this.webService = webService;
    }

    /**
     * Gets file from web service
     *
     * @param mapping Service mapping for 'GET' request
     * @param saveToFile Local file in which the file from the service is saved
     */
    public void downloadFile(String mapping, File saveToFile) {
        //Constructing full URL to which 'GET' request sent
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
    private int sendGetRequest(URL url) {
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

    //Reads response after sending 'GET' request
    private void readResponse(File helloWorldFile) {
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
            throw new WebServiceConnectorException("There is a problem with reading the response", ex);
        }
    }
}