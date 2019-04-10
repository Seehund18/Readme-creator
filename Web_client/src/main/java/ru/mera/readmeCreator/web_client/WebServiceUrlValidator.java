/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.web_client;

import org.apache.commons.validator.routines.UrlValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("WebServiceUrlValidator")
public class WebServiceUrlValidator implements Validator {
    UrlValidator webServiceUrlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String url = value.toString();
        //Checking that url is the server url without path to any resource.
        //For that, using regex pattern, which checks that between beginning and ending of the string are only 2 slashes '/'.
        //For example: http://myService.ru is correct (2 slashes); http://myService.ru/files/HelloWorld.rtf is wrong (4 slashes)

        if(url.matches("^http://[^/]+$") && webServiceUrlValidator.isValid(url)) {
            return;
        }
        FacesMessage msg = new FacesMessage("URL validation failed","Invalid URL format");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(msg);
    }
}
