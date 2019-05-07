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

import java.io.*;
import java.net.HttpURLConnection;
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

    /**
     * Gets file from web service
     * @param mapping service mapping for 'GET' request
     * @param saveToFile local file in which the file from the service is saved
     * @throws WebServiceException some exceptions occurred during downloading of the file
     */
    public static void downloadFile(String mapping, File saveToFile) throws WebServiceException {
        //Sending 'GET' request, reading response code and validating it
        int responseCode = fileWebService.sendGetRequest(mapping);
        if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            //If responseCode is an error code (bigger or equal than 400)
            throw new WebServiceException("Bad response code: " + responseCode);
        }

        //If response code is ok, reading response and closing connection
        fileWebService.readResponseToFile(saveToFile);
        fileWebService.disconnect();
    }

    /**
     * Sends info to the web service and downloads generated file from it
     * @param mapping service mapping for 'POST' request
     * @param saveToFile local file in which the file from the service is saved
     * @throws WebServiceException some exceptions occurred during downloading of the file
     */
    public static void downloadFile(String mapping, String info, File saveToFile) throws WebServiceException {
        //Sending 'POST' request, reading response code and validating it
        int responseCode = fileWebService.sendPostRequest(mapping, info);
        if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            //If responseCode is an error code (bigger or equal than 400)
            throw new WebServiceException("Bad response code: " + responseCode);
        }

        //Sending 'GET' request to download generated file
        downloadFile(mapping, saveToFile);
    }
}