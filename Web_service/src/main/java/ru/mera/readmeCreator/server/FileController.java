package ru.mera.readmeCreator.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

/**
 * Controller of service
 */
@RestController
public class FileController {
    private final Logger log = LoggerFactory.getLogger(FileController.class);

    /**
     * Injected file generator
     */
    @Autowired
    private final FileGenerator fileGenerator;

    public FileController(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }

    /**
     * Handler method for 'GET' file download requests
     * @param name file name
     * @return http response with file
     */
    @GetMapping("/files/{name}")
    public ResponseEntity<FileSystemResource> sendDocument(@PathVariable String name) {
        log.info("Received 'GET' request to generate {} file", name);

        File document;
        try {
            //Trying to generate file with given name
            document = fileGenerator.generate(name);
        } catch (IOException ex) {
            //If some problems occurred, GeneratorException is thrown
            //which will be caught and handled by GeneratorExceptionAdvice
            throw new GeneratorException(ex.getMessage(), ex);
        }
        log.info("Document {} was generated. Sending to client", name);

        //Setting Http response headers
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set(HttpHeaders.CONTENT_TYPE, "application/rtf; charset=utf-8");
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        responseHeader.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(document.length()));

        return new ResponseEntity<>(new FileSystemResource(document), responseHeader, HttpStatus.OK);
    }

    /**
     * Handler method for 'POST' file download requests
     * @param name file name
     * @return http response with file
     */
    @PostMapping("/files/{name}")
    public ResponseEntity<FileSystemResource> sendDocument(@PathVariable String name, @RequestBody UserData userData) {
        log.info("Received 'POST' request to generate {} file", name);
        log.info("Received data:\n{}", userData);

        File document;
        try {
            //Trying to generate file with given name
            document = fileGenerator.generate(name, userData.getInfo());
        } catch (IOException ex) {
            //If some problems occurred, GeneratorException is thrown
            //which will be caught and handled by GeneratorExceptionAdvice
            throw new GeneratorException(ex.getMessage(), ex);
        }
        log.info("Document {} was generated. Sending to client", name);

        //Setting Http response headers
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set(HttpHeaders.CONTENT_TYPE, "application/rtf; charset=utf-8");
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        responseHeader.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(document.length()));

        return new ResponseEntity<>(new FileSystemResource(document), responseHeader, HttpStatus.OK);
    }
}