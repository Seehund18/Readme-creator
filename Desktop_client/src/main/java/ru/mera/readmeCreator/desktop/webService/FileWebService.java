/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.webService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readmeCreator.desktop.interfaces.WebService;

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

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    HttpURLConnection getConnection() {
        return connection;
    }

    /**
     * Constructs new FileWebService. It is assumed that webServiceUrl is correct URL
     * @param webServiceUrl correct web service url
     */
    public FileWebService(URL webServiceUrl) {
        this.url = webServiceUrl;
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

            //Trying to read response
            int responseCode;
            try {
                responseCode = connection.getResponseCode();
            } catch (ConnectException ex) {
                connection.disconnect();
                log.debug("ConnectException was caught");
                return -1;
            }
            return responseCode;
        } catch (IOException ex) {
            connection.disconnect();
            throw new WebServiceException("There is a problem with connection to the server", ex);
        }
    }

    @Override
    public int sendPostRequest(String postMapping, String info) throws WebServiceException {
        //Constructing full URL from url and mapping
        URL fullURL;
        try {
            fullURL = new URL(url.toString() + postMapping);
        } catch (IOException ex) {
            throw new WebServiceException("Can't generate full URL", ex);
        }

        byte[] byteInfo = info.getBytes(StandardCharsets.UTF_8);
        try {
            log.info("Sending 'POST' request to URL: {}", fullURL);

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
            }

            //Trying to read response
            int responseCode;
            try {
                responseCode = connection.getResponseCode();
            } catch (ConnectException ex) {
                connection.disconnect();
                log.debug("ConnectException was caught");
                return -1;
            }
            return responseCode;
        } catch (IOException ex) {
            disconnect();
            throw new WebServiceException("There is a problem with connection to the server", ex);
        }
    }

    public void disconnect() {
        connection.disconnect();
    }
}