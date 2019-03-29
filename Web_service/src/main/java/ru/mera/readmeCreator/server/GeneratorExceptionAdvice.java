package ru.mera.readmeCreator.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GeneratorExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(GeneratorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String generatorExceptionHandler(GeneratorException ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }
}
