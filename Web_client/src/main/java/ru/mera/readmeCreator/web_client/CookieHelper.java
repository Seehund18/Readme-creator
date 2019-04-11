/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.web_client;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class CookieHelper {

    public static void addCookie(String key, String value) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("maxAge", 31536000);
        properties.put("secure", false);
        properties.put("path","/");
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .addResponseCookie(key,value,properties);
    }

    public static String getCookieValue(String key) {
        Map<String, Object> cookies = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestCookieMap();
        Cookie cookie = (Cookie) cookies.get(key);
        if (cookie == null) {
            return "";
        }
        return cookie.getValue();
    }

}
