package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.storage.StorageFileNotFoundException;
import ru.saidgadjiev.aboutme.storage.StorageService;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private static final Logger LOGGER = Logger.getLogger(FileController.class);

    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/logo/{name}", method = RequestMethod.GET)
    public ResponseEntity<Resource> serverFile(@PathVariable("name") String filePath) {
        Resource resource = storageService.loadAsResource(filePath);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
