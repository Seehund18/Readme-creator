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
 * Validator for patch name field.
 * Checks that the field must consist only from alphabetic characters with '_' as a delimiter
 */
@FacesValidator("patchNameFieldValidator")
public class PatchNameFieldValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object patchNameObj) {
        String patchName = patchNameObj.toString();
        if (patchName.matches("[\\p{Alpha}_]+\\p{Alpha}$")) {
            return;
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Invalid patchName");
        throw new ValidatorException(msg);
    }
}
