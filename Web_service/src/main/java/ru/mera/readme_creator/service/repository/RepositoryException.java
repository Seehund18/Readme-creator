package ru.mera.readme_creator.service.repository;

/**
 * Exception from {@link FileRepo}
 * This exception is handled by {@link RepositoryExceptionAdvice}
 */
public class RepositoryException extends Exception {

    RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    RepositoryException(String message) {
        super(message);
    }
}