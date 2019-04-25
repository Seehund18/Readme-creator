/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.service;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Entity which represents data entered by user.
 */
public class UserData {

    private Map<String, String> parameters;
    private String date;
    private List<JiraPair> jiras;

    public Map<String, String> getParameters() {
        return parameters;
    }

    @JsonAnySetter
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public List<JiraPair> getJiras() {
        return jiras;
    }

    public void setJiras(List<JiraPair> jiras) {
        this.jiras = jiras;
    }

    @JsonCreator
    public UserData(Map<String, String> parameters, List<JiraPair> jiras) {
        this.parameters = parameters;
        this.jiras = jiras;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Parameters:\n").append("date: " + date).append(parameters).append("\n\n").append(jiras.toString()).append("\n");
        return str.toString();
    }
}