package ru.mera.readmeCreator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller of service
 */
@RestController
public class ServiceController {
    private final Logger log = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private final FileRepo fileRepo;

    public ServiceController(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    /**
     * Handler method for 'GET' requests
     * @param name file name
     * @return http response with file
     */
    @GetMapping("/files/{name}")
    public ResponseEntity sendDocument(@PathVariable String name) {
        log.info("Received 'GET' request for sending {} file", name);

        File document;
        byte[] fileBytes;
        try {
            //Trying to get file from fileRepo
             fileBytes = fileRepo.getFile(name);
        } catch (IOException ex) {
            //If some problems occurred, RepositoryException is thrown
            //which will be caught and handled by GeneratorExceptionAdvice
            throw new RepositoryException(ex.getMessage(), ex);
        }
        log.info("Document {} was found. Sending to client", name);

        //Setting Http response headers
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set(HttpHeaders.CONTENT_TYPE, "application/rtf; charset=utf-8");
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        responseHeader.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length));

        return new ResponseEntity<>(fileBytes, responseHeader, HttpStatus.OK);
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
            fileRepo.addFile(name, userData);
        } catch (IOException | SQLException ex) {
            //If some problems occurred, RepositoryException is thrown
            //which will be caught and handled by GeneratorExceptionAdvice
            throw new RepositoryException(ex.getMessage(), ex);
        }
        log.info("Document {} was generated", name);
    }

}