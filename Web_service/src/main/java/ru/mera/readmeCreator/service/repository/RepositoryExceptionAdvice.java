package ru.mera.readmeCreator.service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handler for {@link RepositoryException}
 */
@ControllerAdvice
public class RepositoryExceptionAdvice {
    private final Logger log = LoggerFactory.getLogger(RepositoryExceptionAdvice.class);

    @ResponseBody
    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handler(RepositoryException ex) {
        log.error("Repository exception occurred", ex);
    }
}