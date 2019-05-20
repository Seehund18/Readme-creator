/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.service;

import java.util.Objects;

/**
 * Entity which represents jira id and jira description pair
 */
public class JiraPair {
    private String jiraId;
    private String jiraDescrip;

    public JiraPair(String jiraId, String jiraDescrip) {
        this.jiraId = jiraId;
        this.jiraDescrip = jiraDescrip;
    }

    public String getJiraId() {
        return jiraId;
    }
    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public String getJiraDescrip() {
        return jiraDescrip;
    }
    public void setJiraDescrip(String jiraDescrip) {
        this.jiraDescrip = jiraDescrip;
    }

    /**
     * JiraPairs are equal if they have similar jiraId
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof JiraPair) {
            JiraPair pair = (JiraPair) obj;
            return this.jiraId.equals(pair.jiraId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jiraId, jiraDescrip);
    }

    @Override
    public String toString() {
        return "jira_Id: " + jiraId + "\nJira_Description: " + jiraDescrip + "\n";
    }
}