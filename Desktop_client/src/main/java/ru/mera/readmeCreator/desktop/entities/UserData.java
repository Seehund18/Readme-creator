/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity which represents data entered by user.
 * If there is a need to send UserData to service, it's serialized in JSON string format by toString() method
 */
public class UserData {
    private Logger log = LoggerFactory.getLogger(UserData.class);

    /**
     * URL of web service, entered by user. Not the part of JSON serialization
     */
    @JsonIgnore
    private URL webServiceUrl;

    /**
     * Map of parameters, which will be sent to the service. Consists of patchName, date, updateId, releaseVersion
     */
    private Map<String, String> paramMap = new HashMap<>();

    /**
     * List of jira ID and jira description pairs from jiraTable
     */
    private List<JiraPair> jiraList;

    /**
     * Constructs parameters map from parameters, entered by user
     * @param webServiceUrl
     * @param paramTextMap
     * @param jiraList
     */
    public UserData(URL webServiceUrl, Map<String, String > paramTextMap, List<JiraPair> jiraList) {
        String patchName = paramTextMap.get("patchName") +"_"+ paramTextMap.get("releaseVersion") +"."+ paramTextMap.get("issueNumber");
        String updateId = patchName +"."+ paramTextMap.get("updateId");

        paramMap.put("patchName", patchName);
        paramMap.put("date", paramTextMap.get("date"));
        paramMap.put("updateId", updateId);
        paramMap.put("releaseVersion", paramTextMap.get("releaseVersion"));

        this.webServiceUrl = webServiceUrl;
        this.jiraList = jiraList;
    }

    public URL getWebServiceUrl() {
        return webServiceUrl;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public List<JiraPair> getJiraList() {
        return jiraList;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            log.error("Can't convert user data object to json string", ex);
            return "";
        }
    }
}