package ru.mera.readmeCreator.service;

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
public class ServiceController {
    private final Logger log = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private final FileRepo repository;

    public ServiceController(FileRepo repository) {
        this.repository = repository;
    }

    /**
     * Handler method for 'GET' requests
     * @param name file name
     * @return http response with file
     */
    @GetMapping("/files/{name}")
    public ResponseEntity<FileSystemResource> sendDocument(@PathVariable String name) {
        log.info("Received 'GET' request for sending {} file", name);

        File document;
        try {
            //Trying to get file from repository
            document = repository.getFile(name);
        } catch (IOException ex) {
            //If some problems occurred, RepositoryException is thrown
            //which will be caught and handled by GeneratorExceptionAdvice
            throw new RepositoryException(ex.getMessage(), ex);
        }
        log.info("Document {} was found. Sending to client", document.getName());

        //Setting Http response headers
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set(HttpHeaders.CONTENT_TYPE, "application/rtf; charset=utf-8");
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        responseHeader.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(document.length()));

        return new ResponseEntity<>(new FileSystemResource(document), responseHeader, HttpStatus.OK);
    }

    /**
     * Handler method for 'POST' requests
     * @param name file name
     */
    @PostMapping("/files/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendDocument(@PathVariable String name, @RequestBody UserData userData) {
        log.info("Received 'POST' request to addFile {} file", name);
        log.info("Received data:\n{}", userData);

        try {
            //Trying to addFile file with given name
            repository.addFile(name, userData.toString());
        } catch (IOException ex) {
            //If some problems occurred, RepositoryException is thrown
            //which will be caught and handled by GeneratorExceptionAdvice
            throw new RepositoryException(ex.getMessage(), ex);
        }
        log.info("Document {} was generated", name);
    }
}