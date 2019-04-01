package ru.mera.readmeCreator.web_client;

import java.net.URL;

public abstract class WebService {
    protected URL webServiceURL;

    public abstract boolean isAvailable();
    public abstract int sendGetRequest(String getMapping);
}
