package ru.mera.readmeCreator.desktop.validators;

import ru.mera.readmeCreator.desktop.interfaces.Validator;

/**
 * Validator for date field. It expects date in format dd/MM/yyyy
 */
public class DateFieldValidator implements Validator {

    @Override
    public boolean isValid(String date) {
       if (date.matches("\\d{2}/\\d{2}/\\d{4}")) {
           String[] splitDate = date.split("/");
           String day = splitDate[0];
           String month = splitDate[1];
           if (month.equals("02")) {
               return Integer.parseInt(day) <= 29;
           }
           return (Integer.parseInt(day) <= 31) && (Integer.parseInt(month) <= 12);
       }
       return false;
    }
}
