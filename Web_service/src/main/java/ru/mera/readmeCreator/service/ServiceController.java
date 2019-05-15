package ru.mera.readmeCreator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller of service
 */
@RestController
public class ServiceController {
    private final Logger log = LoggerFactory.getLogger(ServiceController.class);

    /**
     * Files repository
     */
    @Autowired
    private final FileRepo fileRepo;

    public ServiceController(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    /**
     * Handler method for 'GET' requests
     * @param fileName file name
     * @throws RepositoryException exception while working with repo
     */
    @GetMapping("/files/{fileName}")
    public ResponseEntity sendDocument(@PathVariable String fileName) throws RepositoryException {
        log.info("Received 'GET' request for sending {} file", fileName);

        byte[] fileBytes = fileRepo.getFile(fileName);
        log.info("Document {} was found. Sending to client", fileName);

        //Setting Http response headers
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set(HttpHeaders.CONTENT_TYPE, "application/rtf; charset=utf-8");
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        responseHeader.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length));

        return new ResponseEntity<>(fileBytes, responseHeader, HttpStatus.OK);
    }

    /**
     * Handler method for 'POST' requests
     * @param fileName name of the file which must be added to repository
     * @throws RepositoryException exception while working with repo
     */
    @PostMapping("/files/{fileName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendDocument(@PathVariable String fileName, @RequestBody UserData userData) throws RepositoryException {
        log.info("Received 'POST' request to addFile {} file", fileName);
        log.info("Received data:\n{}", userData);

        fileRepo.addFile(fileName, userData);

        log.info("Document {} was generated", fileName);
    }
}