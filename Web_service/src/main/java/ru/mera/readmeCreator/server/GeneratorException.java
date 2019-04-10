package ru.mera.readmeCreator.server;

/**
 * Exception in which all exceptions from generators are wrapped
 */
class GeneratorException extends RuntimeException {

    GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
    GeneratorException(String message) {
        super(message);
    }
}
