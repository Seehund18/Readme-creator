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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Validator for date field. It expects date in format dd/MM/yyyy.
 */
@FacesValidator("dateFieldValidator")
public class DateFieldValidator implements Validator {

    /**
     * Number of months with 31 days in it
     */
    private Set<Integer> longMonths = new HashSet<>();

    {
        Collections.addAll(longMonths, 1, 3, 5, 7, 8, 10, 12);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String date = value.toString();

        //Checking dd/mm/yyyy format
        if (date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] splitDate = date.split("/");
            int day = Integer.parseInt(splitDate[0]);
            int month = Integer.parseInt(splitDate[1]);
            int year = Integer.parseInt(splitDate[2]);

            if (month > 0 && month <= 12 && day > 0) {
                //Checking February
                if (month == 2) {
                    checkFebruary(year, day);
                    return;
                }

                //Checking day of month
                if (longMonths.contains(month) && day <= 31) {
                    return;
                } else if (day <= 30) {
                    return;
                }
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Invalid date");
        throw new ValidatorException(msg);
    }

    /**
     * Validates February
     */
    private void checkFebruary(int year, int day) {
        if (isLeapYear(year) && day <= 29) {
            return;
        } else if (day <= 28) {
            return;
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Invalid date format");
        throw new ValidatorException(msg);
    }

    /**
     * Verifies that the year is leap
     */
    private boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if ((year % 100 == 0) && (year % 400 != 0)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
