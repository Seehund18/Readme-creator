package ru.mera.readmeCreator.service;

/**
 * Interface which represents data generator in form of byte arrays
 */
public interface ByteDataGenerator {

    /**
     * Generates byte array using template byte array and data
     * @param data data which is used in generating
     * @param templateBytes byte array of template file
     * @return byte array with generated data
     * @throws GeneratorException exception in generator occurred
     */
    byte[] generateWithTemplate(Object data, byte[] templateBytes) throws GeneratorException;
}