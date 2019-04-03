package ru.mera.readmeCreator.server;

class GeneratorException extends RuntimeException {

    GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
    GeneratorException(String message) {
        super(message);
    }
}
