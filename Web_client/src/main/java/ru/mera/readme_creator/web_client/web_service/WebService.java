package ru.mera.readme_creator.web_client.web_service;

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
     * @return response code or -2 if connection to server was refused
     * @throws WebServiceException problems with sending request
     */
    int sendGetRequest(String getMapping) throws WebServiceException;

    /**
     * Method for sending 'POST' requests to web service
     * @param postMapping mapping for 'POST' request
     * @param data string of data which must be sent to web service
     * @return response code or -2 if connection to server was refused
     * @throws WebServiceException problems with sending request
     */
    int sendPostRequest(String postMapping, String data) throws WebServiceException;
}