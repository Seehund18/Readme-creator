/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class PopupController implements Serializable {

    private String newJiraId;
    private String newJiraDescrip;

    public String getNewJiraId() {
        return newJiraId;
    }

    public void setNewJiraId(String newJiraId) {
        this.newJiraId = newJiraId;
    }

    public String getNewJiraDescrip() {
        return newJiraDescrip;
    }

    public void setNewJiraDescrip(String newJiraDescrip) {
        this.newJiraDescrip = newJiraDescrip;
    }

}
