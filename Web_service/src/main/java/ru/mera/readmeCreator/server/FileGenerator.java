package ru.mera.readmeCreator.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Interface which represents file generator in this service
 */
public interface FileGenerator {

    /**
     * Generates file
     * @param name file name
     * @return generated file
     * @throws NoSuchFileException generator isn't creates such file
     * @throws IOException problem with creating the file
     */
    File generate(String name) throws IOException;
}