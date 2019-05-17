/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Entity which represents data entered by user.
 * If there is a need to send UserData to service, it's serialized in JSON string format by toString() method
 */
@ManagedBean(eager = true)
@SessionScoped
public class UserData implements Serializable {
    private final transient Logger log = LoggerFactory.getLogger(UserData.class);

    @JsonIgnore
    private String url;
    @JsonIgnore
    private String patchName;
    @JsonIgnore
    private String date;
    @JsonIgnore
    private String updateId;
    @JsonIgnore
    private String releaseVer;
    @JsonIgnore
    private String issueNumber;

    private Map<String, String> paramMap = new HashMap<>();
    private List<JiraPair> jiraList = new ArrayList<>();

    @PostConstruct
    public void init() {
        url = CookieHelper.getCookieValue("URL");
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getPatchName() {
        return patchName;
    }
    public void setPatchName(String patchName) {
        this.patchName = patchName;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getReleaseVer() {
        return releaseVer;
    }
    public void setReleaseVer(String releaseVer) {
        this.releaseVer = releaseVer;
    }

    public String getIssueNumber() {
        return issueNumber;
    }
    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public Map<String, String> getParamMap() {
        toMap();
        return paramMap;
    }
    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public List<JiraPair> getJiraList() {
        return jiraList;
    }
    public void setJiraList(List<JiraPair> jiraList) {
        this.jiraList = jiraList;
    }

    @Override
    public String toString() {
        toMap();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            log.error("Can't convert user data object to json string", ex);
            return "";
        }
    }

    /**
     * Transforms string parameters of this class to a map
     */
    private void toMap() {
        String fullPatchName = this.patchName +"_"+ this.releaseVer +"."+ this.issueNumber;
        String fullUpdateId = fullPatchName +"."+ this.updateId;

        paramMap.put("patchName", fullPatchName);
        paramMap.put("date", this.date);
        paramMap.put("updateId", fullUpdateId);
        paramMap.put("releaseVersion", this.releaseVer);
    }
}