package ru.mera.readmeCreator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @throws RepositoryException
     * @return http response with file
     */
    @GetMapping("/files/{name}")
    public ResponseEntity sendDocument(@PathVariable String name) throws RepositoryException {
        log.info("Received 'GET' request for sending {} file", name);

        byte[] fileBytes = fileRepo.getFile(name);
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
     * @param fileName name of the file which must be added to repository
     * @throws RepositoryException
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