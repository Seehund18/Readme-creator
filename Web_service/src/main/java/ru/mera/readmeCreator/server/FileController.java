package ru.mera.readmeCreator.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;


@RestController
public class FileController {
    final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private final FileGenerator fileGenerator;

    public FileController(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }

    @GetMapping("/files/{name}")
    public ResponseEntity<FileSystemResource> sendDocument(@PathVariable String name) {
        File document;
        try {
            document = fileGenerator.generate(name);
        } catch (IOException ex) {
            throw new GeneratorException(ex.getMessage(), ex);
        }
        logger.info("Document is generated");

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set(HttpHeaders.CONTENT_TYPE, "application/rtf; charset=utf-8");
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        responseHeader.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(document.length()));

        return new ResponseEntity<>(new FileSystemResource(document), responseHeader, HttpStatus.OK);
    }
}