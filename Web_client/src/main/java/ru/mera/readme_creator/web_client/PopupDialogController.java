package ru.mera.readme_creator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class PopupDialogController implements Serializable {
    private final Logger log = LoggerFactory.getLogger(MainController.class);

    private String jiraId;
    private String jiraDescrip;

    private String file;
    private int editIndex;

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }


    public void setDialogType(String dialogType) {
        if (dialogType.equals("Add")) {
            log.info("Setting file to /AddButton.xhtml");
            file = "/AddButton.xhtml";
        } else if (dialogType.equals("Edit")) {
            file = "/EditButton.xhtml";
        }
    }
}
