package ru.mera.readmeCreator.service;

/**
 * Exception from {@link FileRepo}
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