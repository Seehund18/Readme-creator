/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.web_client.validators;

import org.apache.commons.validator.routines.UrlValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validator for URL.
 *
 * Checks that url is just server url without path to any resource.
 * For that, using regex pattern, which checks that between beginning and ending of the string are only 2 slashes '/'.
 * For example: http://myService.ru is correct (2 slashes); http://myService.ru/files/HelloWorld.rtf is wrong (4 slashes)
 * Also using UrlValidator class from apache.commons library to check the url
 */
@FacesValidator("webServiceUrlValidator")
public class WebServiceUrlValidator implements Validator {
    //from apache.commons library
    private UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);

    @Override
    public void validate(FacesContext context, UIComponent component, Object urlObj) {
        String url = urlObj.toString();
        if(url.matches("^http://[^/]+$") && urlValidator.isValid(url)) {
            return;
        }
        //If value is not valid, setting message to "Invalid URL format"
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Invalid URL format");
        throw new ValidatorException(msg);
    }
}
