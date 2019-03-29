package ru.mera.readmeCreator.server;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class FileController {

    @Autowired
    private final FileGenerator fileGenerator;

    public FileController(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }

    @GetMapping("/getFile")
    public ResponseEntity<FileSystemResource> sendDocument() {
        File document;
        try {
            document = fileGenerator.generate();
        } catch (Exception ex) {
            throw new GeneratorException(ex);
        }

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set(HttpHeaders.CONTENT_TYPE, "application/rtf; charset=utf-8");
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        responseHeader.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(document.length()));

        return new ResponseEntity<>(new FileSystemResource(document), responseHeader, HttpStatus.OK);
    }
}