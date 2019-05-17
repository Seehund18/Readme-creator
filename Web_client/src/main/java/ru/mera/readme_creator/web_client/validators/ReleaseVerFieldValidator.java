/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validator for releaseVersionField. It expects version in format 5.6.7.3
 */
@FacesValidator("releaseVerFieldValidator")
public class ReleaseVerFieldValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object versionObj) {
        String version = versionObj.toString();

        if (version.matches("(\\d\\.)*\\d$")) {
            return;
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Invalid format");
        throw new ValidatorException(msg);
    }
}
