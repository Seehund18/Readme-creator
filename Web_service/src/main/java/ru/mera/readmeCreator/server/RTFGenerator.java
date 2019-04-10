package ru.mera.readmeCreator.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.NoSuchFileException;

/**
 * File generator in .rtf format
 */
public class RTFGenerator implements FileGenerator {
    private RtfPrinter printer = new RtfPrinter();
    private final Logger log = LoggerFactory.getLogger(RTFGenerator.class);

    @Override
    public File generate(String name) throws IOException {
        //Creating files directory, if one is missing
        File filesDirectory = new File("files");
        filesDirectory.mkdir();

        //Searching for file
        if(name.equals("Hello_world.rtf")) {
            return generateHelloWorldFile();
        }

        //File wasn't found
        throw new NoSuchFileException("Server isn't generates this kind of file " + name);
    }

    //Generates "Hello world" file
    private File generateHelloWorldFile() throws IOException {
        File helloWorldFile = new File("files/Hello_world.rtf");

        if(!helloWorldFile.exists()) {
            log.debug("Prints into {} file", helloWorldFile);
            printer.printHelloWorld(helloWorldFile);
        }
        return helloWorldFile;
    }
}