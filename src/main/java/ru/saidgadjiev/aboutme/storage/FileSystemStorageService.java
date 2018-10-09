package ru.saidgadjiev.aboutme.storage;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.aboutme.properties.StorageProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    private UidGenerator uidGenerator = new UidGenerator() {

        private AtomicInteger atomicInteger = new AtomicInteger();

        @Override
        public synchronized int nextUid() {
            return atomicInteger.incrementAndGet();
        }
    };

    @Autowired
    public FileSystemStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getDir());
    }

    @Override
    public String store(MultipartFile file) {
        String name = FilenameUtils.getBaseName(file.getOriginalFilename());
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = name + "_" + uidGenerator.nextUid() + "." + ext;

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + fileName);
            }
            if (fileName.contains("..")) {
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + fileName);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fileName, e);
        }

        return fileName;
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileName, e);
        }
    }

    @Override
    public Path load(String fileName) {
        return rootLocation.resolve(fileName);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
