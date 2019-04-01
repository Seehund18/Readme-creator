package ru.mera.readmeCreator.web_client;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@ManagedBean(eager = true)
public class FileGeneratorWebService extends WebService {

    @PostConstruct
    public void init() {
        try {
            webServiceURL = new URL("http://localhost:8080");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAvailable() {
        return sendGetRequest("/files/HelloWorld.rtf") < 500;
    }

    @Override
    public int sendGetRequest(String getMapping) {
        HttpURLConnection connection = null;
        try  {
            URL fullURL = new URL(webServiceURL.toString() + getMapping);

            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "web_client");

            System.out.println("\nSending 'GET' request to URL : " + fullURL);
            System.out.println("Response Code : " + connection.getResponseCode());

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