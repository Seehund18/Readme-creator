package ru.mera.readmeCreator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfPara.p;

/**
 * File generator in .rtf format
 */
public class RTFGenerator implements FileGenerator {
    private final Logger log = LoggerFactory.getLogger(RTFGenerator.class);

    @Override
    public File generate(File generatedFile, String info) throws IOException {
        log.info("Printing into {} file", generatedFile.getName());
        print(generatedFile, info);
        return generatedFile;
    }

    /**
     * Prints info into file with centered alignment
     * @param fileToPrint file in which info will be printed
     * @param info information to print
     * @throws IOException can't print to file
     */
    private void print(File fileToPrint, String info) throws IOException {
        try (FileWriter out = new FileWriter(fileToPrint)) {
            rtf().section(p(info).alignCentered())
                    .out(out);
        }
    }
}