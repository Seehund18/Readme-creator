package ru.mera.readmeCreator.desktop.validators;

import ru.mera.readmeCreator.desktop.interfaces.Validator;

/**
 * Validator for date field. It expects date in format dd/mm/yyyy
 */
public class DateFieldValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        return (value.matches("\\d{2}/\\d{2}/\\d{4}"));
    }
}
