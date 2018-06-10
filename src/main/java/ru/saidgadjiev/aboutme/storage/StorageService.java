package ru.saidgadjiev.aboutme.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void init();

    String store(String parentDir, MultipartFile file);

    Resource loadAsResource(String parentDir, String fileName);

    Path load(String parentDir, String fileName);

    interface UidGenerator {
        int nextUid();
    }
}
