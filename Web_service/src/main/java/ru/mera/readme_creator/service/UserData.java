/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.service;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.Map;

/**
 * Entity which represents user data from clients
 */
public class UserData {

    /**
     * Map of parameters. Consists of patch name, date, update id, release version
     */
    private Map<String, String> paramMap;

    /**
     * List of jiras
     */
    private List<JiraPair> jiraList;

    public Map<String, String> getParamMap() {
        return paramMap;
    }
    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public String getPatchName() {
        return paramMap.get("patchName");
    }

    public String getDate() {
        return paramMap.get("date");
    }

    public String getUpdateId() {
        return paramMap.get("updateId");
    }

    public String getReleaseVer() {
        return paramMap.get("releaseVersion");
    }

    public List<JiraPair> getJiraList() {
        return jiraList;
    }
    public void setJiraList(List<JiraPair> jiraList) {
        this.jiraList = jiraList;
    }

    @JsonCreator
    public UserData(Map<String, String> paramMap, List<JiraPair> jiraList) {
        this.paramMap = paramMap;
        this.jiraList = jiraList;
    }

    @Override
    public String toString() {
        return "Parameters:\n" + paramMap + "\n\n" + jiraList.toString() + "\n";
    }
}