package ru.mera.readmeCreator.service;

/**
 * Exception in which all exceptions from repository are wrapped
 */
class RepositoryException extends RuntimeException {

    RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
    RepositoryException(String message) {
        super(message);
    }
}