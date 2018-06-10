package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.aboutme.model.ProjectDetails;
import ru.saidgadjiev.aboutme.model.ResponseMessage;
import ru.saidgadjiev.aboutme.storage.StorageFileNotFoundException;
import ru.saidgadjiev.aboutme.storage.StorageService;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private static final Logger LOGGER = Logger.getLogger(FileController.class);

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/logo", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage<String>> uploadLogo(@RequestPart(value="file") MultipartFile file) {
        LOGGER.debug("Upload file " + file.getOriginalFilename());

        String logoPath = storageService.store("logo", file);

        return ResponseEntity.ok(new ResponseMessage<>("", logoPath));
    }

    @RequestMapping(value = "/logo/{name}", method = RequestMethod.GET)
    public ResponseEntity<Resource> serverFile(@PathVariable("name") String logo) {
        Resource resource = storageService.loadAsResource("logo", logo);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
