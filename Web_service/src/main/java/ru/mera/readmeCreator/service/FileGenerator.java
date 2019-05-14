package ru.mera.readmeCreator.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

/**
 * Interface which represents file generator in this service
 */
public interface FileGenerator {

    /**
     * Generates file with given info
     * @param generatedFile file which must be generated
     * @param info information to write to file
     * @return generated file
     * @throws NoSuchFileException generator isn't creates such file
     * @throws IOException problem with creating the file
     */
    File generate(File generatedFile, String info) throws IOException;

    byte[] generateOnTemplate(File generatedFile, Object data, byte[] templateFile) throws GeneratorException;
}