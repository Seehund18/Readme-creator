package ru.mera.readmeCreator.web_client;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class WebService {
    protected URL webServiceURL;

    protected void setWebServiceURL(String webServiceURL) throws MalformedURLException {
        this.webServiceURL = new URL(webServiceURL);
    }

    public abstract boolean isAvailable();
    public abstract int sendGetRequest(String getMapping);
}
