package ru.mera.readmeCreator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of WebService Connector.
 * Represents connection to file generator service.
 * It's responsible for sending Http requests to web service, establishing connection with it.
 **/
@ManagedBean(eager = true)
@SessionScoped
public class FileWebServiceConnector extends WebServiceConnector implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(UserData.class);

    @Override
    public int sendGetRequest(String getMapping) throws WebServiceConnectorException {
        URL fullURL;
        try {
            fullURL = new URL(webService.toString() + getMapping);
        } catch (IOException ex) {
            throw new WebServiceConnectorException("Can't generate full URL", ex);
        }

        try  {
            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "web_client");

            log.info("Sending 'GET' request to URL: {}", fullURL);
            int responseCode;
            try {
                responseCode = connection.getResponseCode();
            } catch (ConnectException ex) {
                //Service refused connection
                connection.disconnect();
                log.debug("ConnectException was caught\n");
                return -1;
            }
            log.info("Response code: {}", responseCode);
            return responseCode;
        } catch (IOException ex) {
            connection.disconnect();
            throw new WebServiceConnectorException("There is a problem with connection to the server", ex);
        }
    }

    @Override
    public int sendPostRequest(String postMapping, String info) throws WebServiceConnectorException {
        //Constructing full URL from webService url and mapping
        URL fullURL;
        try {
            fullURL = new URL(webService.toString() + postMapping);
        } catch (IOException ex) {
            throw new WebServiceConnectorException("Can't generate full URL", ex);
        }

        byte[] byteInfo = info.getBytes(StandardCharsets.UTF_8);
        try {
            log.info("Sending 'POST' request to URL: {}", fullURL);

            //Setting request headers
            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "web_client");
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
            connection.disconnect();
            throw new WebServiceConnectorException("There is a problem with connection to the server", ex);
        }
    }
}