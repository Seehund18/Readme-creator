package ru.mera.readme_creator.web_client.webService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mera.readme_creator.web_client.UserData;

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
public class FileWebService implements WebService, Serializable {
    private static final Logger log = LoggerFactory.getLogger(UserData.class);

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
        URL fullURL;
        try {
            fullURL = new URL(url.toString() + getMapping);
        } catch (IOException ex) {
            throw new WebServiceException("Can't generate full URL", ex);
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
            log.info("Response code: {}\n", responseCode);
            return responseCode;
        } catch (IOException ex) {
            disconnect();
            throw new WebServiceException("There is a problem with connection to the server", ex);
        }
    }

    @Override
    public int sendPostRequest(String postMapping, String info) throws WebServiceException {
        //Constructing full URL from webService url and mapping
        URL fullURL;
        try {
            fullURL = new URL(url.toString() + postMapping);
        } catch (IOException ex) {
            throw new WebServiceException("Can't generate full URL", ex);
        }

        byte[] byteInfo = info.getBytes(StandardCharsets.UTF_8);
        try {
            log.info("Sending 'POST' request to URL: {}", fullURL);
            log.info("Sent information: {}", info);

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
                os.flush();
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
            log.info("Response code: {}\n", responseCode);
            return responseCode;
        } catch (IOException ex) {
            disconnect();
            throw new WebServiceException("There is a problem with connection to the server", ex);
        }
    }

    void disconnect() {
        connection.disconnect();
    }
}