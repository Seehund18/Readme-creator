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

/**
 * Parameters users enter
 */
public enum Parameters {
    URL(false),
    DATE(true),
    RELEASE_VERSION( true),
    ISSUE_NUMBER( false),

    PATCH_NAME(true, paramMap -> {
        Parameters[] neededParams = { Parameters.valueOf("PATCH_NAME"),
                Parameters.RELEASE_VERSION, Parameters.ISSUE_NUMBER};
        if (!validateMap(paramMap, neededParams)) {
            throw new IllegalArgumentException("Map of parameters doesn't consist of required parameters." +
                    "Required: " + Arrays.toString(neededParams) + "\n" +
                    "Found: " + paramMap);
        }
        return Optional.of(paramMap.get(Parameters.valueOf("PATCH_NAME"))
                + "_" + paramMap.get(Parameters.RELEASE_VERSION)
                + "." + paramMap.get(Parameters.ISSUE_NUMBER));
    }),

    UPDATE_ID(true, paramMap -> {
        Parameters[] neededParams = { Parameters.PATCH_NAME, Parameters.valueOf("UPDATE_ID")};
        if (!validateMap(paramMap, neededParams)) {
            throw new IllegalArgumentException("Map of parameters doesn't consist of required parameters." +
                    "Required: " + Arrays.toString(neededParams) + "\n" +
                    "Found: " + paramMap);
        }
        return Optional.of(Parameters.PATCH_NAME.fullConstructor.apply(paramMap).get()
                + "." + paramMap.get(Parameters.valueOf("UPDATE_ID")));
    });

    /**
     * Flag shows whether is parameter must be sent to service
     */
    private final boolean sendToService;

    /**
     * Function describes how to construct full parameter using existing.
     * Will return empty optional if parameter doesn't need to be constructed.
     * Map<Parameters, String> is a map with parameters values.
     */
    private final Function<Map<Parameters, String>, Optional<String>> fullConstructor;

    Parameters(boolean sendToService, Function<Map<Parameters, String>, Optional<String>> fullConstructor) {
        this.sendToService = sendToService;
        this.fullConstructor = fullConstructor;
    }

    Parameters(boolean sendToService) {
        //If parameter already consists all needed info, fullConstructor returns empty Optional
        this(sendToService, paramMap -> Optional.empty());
    }

    public boolean isSendToService() {
        return sendToService;
    }

    public Function<Map<Parameters, String>, Optional<String>> getFullConstructor() {
        return fullConstructor;
    }

    /**
     * Constructs name of enum parameter, which will be sent to service.
     * @return parameter's name transformed from UPPER_SNAKE_CASE to camelCase
     */
    public String constructName() {
        StringBuilder result = new StringBuilder();
        boolean firstAfterUnderscore = false;
        for (char sign: this.toString().toCharArray()) {
            if (firstAfterUnderscore) {
                result.append(sign);
                firstAfterUnderscore = false;
            } else if (sign == '_') {
                firstAfterUnderscore = true;
            } else {
                result.append(Character.toLowerCase(sign));
            }
        }
        return result.toString();
    }

    /**
     * Validates parameters map
     * @param paramMap map with Parameters as keys and user's entered data as value
     * @param neededParams another Parameters which will be used in construction     *
     */
    private static boolean validateMap(Map<Parameters, String> paramMap, Parameters... neededParams) {
        for (Parameters param: neededParams) {
           if (!paramMap.containsKey(param)) {
               return false;
           }
        }
        return true;
    }
}
