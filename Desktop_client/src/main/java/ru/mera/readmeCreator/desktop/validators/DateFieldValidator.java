package ru.mera.readmeCreator.desktop.validators;

import ru.mera.readmeCreator.desktop.interfaces.Validator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Validator for date field. It expects date in format dd/MM/yyyy
 */
public class DateFieldValidator implements Validator {
    /**
     * Number of months with 31 days in it
     */
    private Set<Integer> longMonths = new HashSet<>();

    {
        Collections.addAll(longMonths, 1, 3, 5, 7, 8, 10, 12);
    }

    @Override
    public boolean isValid(String date) {

       if (date.matches("\\d{2}/\\d{2}/\\d{4}")) {
           String[] splitDate = date.split("/");
           int day = Integer.parseInt(splitDate[0]);
           int month = Integer.parseInt(splitDate[1]);
           int year = Integer.parseInt(splitDate[2]);

           if (month > 0 && month <= 12 && day > 0) {
               //Checking February
               if (month == 2) {
                   return checkFebruary(year, day);
               }

               //Checking day of month
               if (longMonths.contains(month)) {
                   return day <= 31;
               } else
                   return day <= 30;
           }
       }
       return false;
    }

    /**
     * Validates February
     */
    private boolean checkFebruary(int year, int day) {
        if (isLeapYear(year)) {
            return day <= 29;
        } else {
            return day <= 28;
        }
    }

    /**
     * Verifies that the year is a leap
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
