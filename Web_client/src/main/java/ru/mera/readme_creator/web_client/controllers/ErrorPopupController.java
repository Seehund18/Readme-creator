/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * Controller for an error popup window
 */
@ManagedBean
@SessionScoped
public class ErrorPopupController implements Serializable {

    /**
     * Flag which shows is error popup should be rendered or not
     */
    private boolean rendered;

    /**
     * Message which will be shown to the user
     */
    private String errorMsg;

    public boolean isRendered() {
        return rendered;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Shows error popup to the user
     * @param errorMsg message to show
     */
    public void showError(String errorMsg) {
        this.errorMsg = errorMsg;
        rendered = true;
    }

    /**
     * Hides error popup
     */
    public void hide() {
        rendered = false;
    }
}