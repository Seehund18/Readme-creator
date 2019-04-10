package ru.mera.readmeCreator.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handler for GeneratorExceptions
 */
@ControllerAdvice
public class GeneratorExceptionAdvice {
    private final Logger log = LoggerFactory.getLogger(GeneratorExceptionAdvice.class);

    @ResponseBody
    @ExceptionHandler(GeneratorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void generatorExceptionHandler(GeneratorException ex) {
        log.error("Generator exception occurred", ex);
    }
}