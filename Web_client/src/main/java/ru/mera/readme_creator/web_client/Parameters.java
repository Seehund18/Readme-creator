/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client;

//TODO Может использовать value как биндинг для ввода?
public enum Parameters {
    URL("url", false),
    PATCH_NAME("patchName", true),
    DATE("date", true),
    UPDATE_ID("updateId", true),
    RELEASE_VER("releaseVersion", true),
    ISSUE_NUM("issueNumber", false);

    private String name;
    private String value = "Test value";
    private boolean sendToService;

    Parameters(String name, boolean sendToService) {
        this.name = name;
        this.sendToService = sendToService;
    }

    public String getName() {
        return name;
    }

    public boolean isSendToService() {
        return sendToService;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
