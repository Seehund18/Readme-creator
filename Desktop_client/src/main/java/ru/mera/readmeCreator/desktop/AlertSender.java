/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Indicates that class that implements this interface is able to send alerts to user
 */
public interface AlertSender {

    /**
     * Creates and shows alert to user
     * @param text text to show
     * @param type type of alert
     */
    default void sendAlert(String text, Alert.AlertType type) {
        Alert alert = new Alert(type, text, ButtonType.OK);
        alert.showAndWait();
    }

}
