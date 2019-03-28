package ru.mera.yakimychev;

class GeneratorException extends RuntimeException {
    GeneratorException(Throwable cause) {
        super("Generator exception occurred", cause);
    }
}
