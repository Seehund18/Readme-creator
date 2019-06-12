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
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * Entity which represents data entered by user.
 * If there is a need to send UserData to service, it's serialized in JSON string format by toString() method
 */
@Named
@SessionScoped
public class UserData implements Serializable {
    private final transient Logger log = LoggerFactory.getLogger(UserData.class);

    @JsonIgnore
    private Map<Parameters, String> viewParamMap = new EnumMap<>(Parameters.class);

    private Map<String, String> paramMap = new HashMap<>();
    private List<JiraPair> jiraList = new ArrayList<>();

    @PostConstruct
    public void init() {
        viewParamMap.put(Parameters.URL, CookieHelper.getCookieValue("URL"));
    }

    public Map<Parameters, String> getViewParamMap() {
        return viewParamMap;
    }
    public void setViewParamMap(Map<Parameters, String> viewParamMap) {
        this.viewParamMap = viewParamMap;
    }

    public Map<String, String> getParamMap() {
        fillParamMap();
        return paramMap;
    }

    public List<JiraPair> getJiraList() {
        return jiraList;
    }
    public void setJiraList(List<JiraPair> jiraList) {
        this.jiraList = jiraList;
    }

    @Override
    public String toString() {
        fillParamMap();
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
    private void fillParamMap() {
        paramMap = viewParamMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isSendToService())
                .map(entry -> {
                    Optional<String> optionalValue = entry.getKey().getFullConstructor().apply(viewParamMap);
                    String value = optionalValue.orElseGet(entry::getValue);
                    return new AbstractMap.SimpleEntry<>(entry.getKey().getName(), value);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}