package ru.mera.readmeCreator.desktop;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnector {
    private final URL serverUrl;

    ServerConnector(URL serverUrl) {
        this.serverUrl = serverUrl;
    }

    void sendGetRequest(String getMapping) {
        HttpURLConnection connection = null;
        try {
            URL fullURL = new URL(serverUrl.toString() + getMapping);

            connection = (HttpURLConnection) fullURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "desktop");

            System.out.println("\nSending 'GET' request to URL : " + fullURL);
            System.out.println("Response Code : " + connection.getResponseCode());

            File helloWorldFile = new File("Hello World.rtf");
            readResponse(connection, helloWorldFile);
        } catch (IOException ex) {
            throw new ServerConnectorException("There is a problem with connection to the server", ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void readResponse(HttpURLConnection connection, File helloWorldFile) throws IOException {
        String inputLine;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             FileWriter out = new FileWriter(helloWorldFile)) {

            inputLine = in.readLine();
            while (inputLine != null) {
                out.write(inputLine + "\n");
                inputLine = in.readLine();
            }
            out.flush();
        }
    }
}