/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.desktop.web_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readme_creator.desktop.interfaces.WebService;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of WebService. Represents file web service.
 */
public class FileWebService implements WebService {
    private final Logger log = LoggerFactory.getLogger(FileWebService.class);

    /**
     * URL of web service
     */
    private URL url;

    /**
     * Connection to the web service
     */
    private HttpURLConnection connection;

    /**
     * Constructs new FileWebService. It is assumed that webServiceUrl is correct URL.
     * Validation of URL must be performed before constructing.
     * @param webServiceUrl correct web service url
     */
    public FileWebService(URL webServiceUrl) {
        this.url = webServiceUrl;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public int sendGetRequest(String getMapping) throws WebServiceException {
        //Constructing full URL from url url and mapping
        URL fullURL;
        try {
            fullURL = new URL(url.toString() + getMapping);
        } catch (IOException ex) {
            throw new WebServiceException("Can't generate full URL", ex);
        }

        try {
            log.info("Sending 'GET' request to URL: {}", fullURL);

            //Setting request headers
            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "desktop");

            //Trying to get response code
            int responseCode = getResponse();
            log.info("Response code {}", responseCode);
            return responseCode;
        } catch (IOException ex) {
            disconnect();
            throw new WebServiceException("There is a problem with connection to the server", ex);
        }
    }

    @Override
    public int sendPostRequest(String postMapping, String data) throws WebServiceException {
        //Constructing full URL from url and mapping
        URL fullURL;
        try {
            fullURL = new URL(url.toString() + postMapping);
        } catch (IOException ex) {
            throw new WebServiceException("Can't generate full URL", ex);
        }

        byte[] byteInfo = data.getBytes(StandardCharsets.UTF_8);
        try {
            log.info("Sending 'POST' request to URL: {}", fullURL);
            log.info("Sent information: {}", data);

            //Setting request headers
            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "desktop");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setFixedLengthStreamingMode(byteInfo.length);

            //Sending info
            try (OutputStream os = connection.getOutputStream()) {
                os.write(byteInfo);
                os.flush();
            }

            //Trying to read response code
            int responseCode = getResponse();
            log.info("Response code {}", responseCode);
            return responseCode;
        } catch (IOException ex) {
            disconnect();
            throw new WebServiceException("There is a problem with connection to the server", ex);
        }
    }

    /**
     * Gets response code from service
     * @return return value of connection.getResponseCode() method or -2 if connection was refused
     * @throws IOException if an error occurred connecting to the server
     */
    private int getResponse() throws IOException {
        try {
            return connection.getResponseCode();
        } catch (ConnectException ex) {
            //Service refused connection
            connection.disconnect();
            log.debug("ConnectException was caught\n");
            return -2;
        }
    }

    /**
     * Reads response from service to file
     * @param saveToFile file to which save the response
     * @throws WebServiceException problem with reading the response
     */
    void readResponseToFile(File saveToFile) throws WebServiceException {
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
            throw new WebServiceException("There is a problem with reading the response", ex);
        }
    }

    void disconnect() {
        connection.disconnect();
    }
}