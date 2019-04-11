package ru.mera.readmeCreator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean(eager = true)
@SessionScoped
public class FileGeneratorWebService extends WebService {
    private static final Logger log = LoggerFactory.getLogger(UserData.class);

    @Override
    public boolean isAvailable() {
        return sendGetRequest("") != -1;
    }

    @Override
    public int sendGetRequest(String getMapping) {
        HttpURLConnection connection = null;
        try  {
            URL fullURL = new URL(webServiceURL.toString() + getMapping);

            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "web_client");

            log.info("Sending 'GET' request to URL : " + fullURL + "\n");
            try {
                connection.getResponseCode();
            } catch (ConnectException ex) {
                log.info("ConnectException was caught\n");
                return -1;
            }
            log.info("Response Code : " + connection.getResponseCode() + "\n");

            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return HttpURLConnection.HTTP_INTERNAL_ERROR;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}