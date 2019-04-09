/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.desktop;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * ChangeListener which and adds ability to validate new value before change.
 * User can choose different behavior of listener according to its validation.
 */
public interface ValidatedChangeListener extends ChangeListener<String> {

    @Override
    void changed(ObservableValue<? extends String> observable, String oldValue, String newValue);

    boolean isValid(String value);
}
