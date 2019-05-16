/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

/**
 * Class which simplify working with cookies in the program
 */
public class CookieHelper {

    /**
     * Adds permanent cookies.
     * @param key name of the cookie
     * @param cookieValue cookie value
     * @param maxAge time in seconds how long cookie will live
     */
    public static void addPermanentCookie(String key, String cookieValue, int maxAge) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("maxAge", maxAge);
        properties.put("secure", false);
        properties.put("path","/");
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .addResponseCookie(key, cookieValue, properties);
    }

    /**
     * Gets cookies
     * @param cookieKey name of the cookie
     * @return value of the cookie or empty string if there is no such cookie
     */
    public static String getCookieValue(String cookieKey) {
        Map<String, Object> cookies = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestCookieMap();
        Cookie cookie = (Cookie) cookies.get(cookieKey);
        if (cookie == null) {
            return "";
        }
        return cookie.getValue();
    }
}
