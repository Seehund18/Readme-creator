package ru.mera.readmeCreator.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceConnector {
    private final URL webService;
    private HttpURLConnection connection;
    private Logger log = LoggerFactory.getLogger(WebServiceConnector.class);

    WebServiceConnector(URL webService) {
        this.webService = webService;
    }

    public void getFile(String mapping, File saveToFile) {
        URL fullURL;
        try {
            fullURL = new URL(webService.toString() + mapping);
        } catch (IOException ex) {
            throw new WebServiceConnectorException("Can't generate full URL", ex);
        }
        int responseCode = sendGetRequest(fullURL);
        log.info("Response code from the server: {}", responseCode);

        if (responseCode >= 400) {
            throw new WebServiceConnectorException("Bad response code: " + responseCode);
        }
        readResponse(saveToFile);
        connection.disconnect();
    }

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