package ru.mera.readmeCreator.server;

class GeneratorException extends RuntimeException {
    GeneratorException(Throwable cause) {
        super("Generator exception occurred", cause);
    }
}
