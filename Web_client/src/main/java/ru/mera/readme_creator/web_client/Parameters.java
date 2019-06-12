/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client;

import java.util.*;
import java.util.function.Function;

public enum Parameters {
    URL("url", false),
    DATE("date", true),
    RELEASE_VER("releaseVersion", true),
    ISSUE_NUM("issueNumber", false),

    PATCH_NAME("patchName", true, paramMap -> {
        Parameters[] neededParams = { Parameters.valueOf("PATCH_NAME"),Parameters.RELEASE_VER, Parameters.ISSUE_NUM};
        if (!validateMap(paramMap, neededParams)) {
            throw new IllegalArgumentException("Map of parameters doesn't consist of required parameters." +
                    "Required: " + Arrays.toString(neededParams) + "\n" +
                    "Found: " + paramMap);
        }

        return Optional.of(paramMap.get(Parameters.valueOf("PATCH_NAME"))
                + "_" + paramMap.get(Parameters.RELEASE_VER)
                + "." + paramMap.get(Parameters.ISSUE_NUM));
    }),

    UPDATE_ID("updateId", true, paramMap -> {
        Parameters[] neededParams = { Parameters.PATCH_NAME, Parameters.valueOf("UPDATE_ID")};
        if (!validateMap(paramMap, neededParams)) {
            throw new IllegalArgumentException("Map of parameters doesn't consist of required parameters." +
                    "Required: " + Arrays.toString(neededParams) + "\n" +
                    "Found: " + paramMap);
        }

        return Optional.of(Parameters.PATCH_NAME.fullConstructor.apply(paramMap).get()
                + "." + paramMap.get(Parameters.valueOf("UPDATE_ID")));
    });

    private final String name;
    private final boolean sendToService;
    private final Function<Map<Parameters, String>, Optional<String>> fullConstructor;

    Parameters(String name, boolean sendToService, Function<Map<Parameters, String>, Optional<String>> fullConstructor ) {
        this.name = name;
        this.sendToService = sendToService;
        this.fullConstructor = fullConstructor;
    }

    Parameters(String name, boolean sendToService) {
        this(name, sendToService, paramMap -> Optional.empty());
    }

    public String getName() {
        return name;
    }

    public boolean isSendToService() {
        return sendToService;
    }

    public Function<Map<Parameters, String>, Optional<String>> getFullConstructor() {
        return fullConstructor;
    }

    private static boolean validateMap(Map<Parameters, String> paramMap, Parameters... neededParams) {
        for (Parameters param: neededParams) {
           if (!paramMap.containsKey(param)) {
               return false;
           }
        }
        return true;
    }
}
