/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.web_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Holds all of the data, entered by user
 */
@ManagedBean(eager = true)
@SessionScoped
public class UserData implements Serializable {
    private final Logger log = LoggerFactory.getLogger(UserData.class);

    /**
     * Url entered in webServiceURL
     */
    private String url;

    /**
     * Text entered in the userText area
     */
    private String text;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        log.info("Setting url in UserData to {}\n", url);
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * After constructing of this object, sets url field to value from URL cookie
     */
    @PostConstruct
    private void init() {
        url = CookieHelper.getCookieValue("URL");
    }


}
