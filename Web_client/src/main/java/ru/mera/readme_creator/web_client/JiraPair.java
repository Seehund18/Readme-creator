package ru.mera.readme_creator.web_client;

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
    public String toString() {
        return "Jira id: " + jiraId + "; Jira description: " + jiraDescrip;
    }
}
