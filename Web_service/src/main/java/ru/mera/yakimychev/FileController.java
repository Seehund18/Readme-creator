package ru.mera.yakimychev;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class FileController {
    private final FileGenerator fileGenerator;

    public FileController(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }

    @GetMapping("/getFile")
    public HttpEntity<FileSystemResource> sendDocument() {

        File document = fileGenerator.generate();

        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("charset", "utf-8");
        responseHeader.setContentType(MediaType.valueOf("application/rtf"));
        responseHeader.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + document.getName());
        responseHeader.setContentLength(document.length());

        return new HttpEntity<>(new FileSystemResource(document), responseHeader);
    }
}
