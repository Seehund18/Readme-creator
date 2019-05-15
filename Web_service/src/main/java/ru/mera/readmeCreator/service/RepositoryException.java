package ru.mera.readmeCreator.service;

/**
 * Exception in which all exceptions from repository are wrapped.
 * This exception is handled by {@link RepositoryExceptionAdvice}
 */
class RepositoryException extends Exception {

    RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    RepositoryException(String message) {
        super(message);
    }
}