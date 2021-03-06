/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.desktop.interfaces;

import ru.mera.readme_creator.desktop.web_service.WebServiceException;

/**
 * Interface for web service
 */
public interface WebService {

    /**
     * Defines when this web service is available. By default, sending 'GET' request
     * directly to service URL
     * @return true - service is available and false otherwise
     * @throws WebServiceException problems with sending request
     */
    default boolean isServiceAvailable() throws WebServiceException {
        return sendGetRequest("") != -2;
    }

    /**
     * Method for sending 'GET' requests to web service
     * @param getMapping mapping for 'GET' request
     * @return response code or -1 if connection to server was refused
     * @throws WebServiceException problems with sending request
     */
    int sendGetRequest(String getMapping) throws WebServiceException;

    /**
     * Method for sending 'PUT' requests to web service
     * @param postMapping mapping for 'PUT' request
     * @param data string of data which must be sent to web service
     * @return response code or -1 if connection to server was refused
     * @throws WebServiceException problems with sending request
     */
    int sendPutRequest(String postMapping, String data) throws WebServiceException;

}