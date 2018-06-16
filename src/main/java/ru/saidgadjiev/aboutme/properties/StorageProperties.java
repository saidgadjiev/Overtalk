package ru.saidgadjiev.aboutme.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class StorageProperties {

    @Value("${app.upload.dir}")
    private String uploadDir = "upload-dir";

    public String getUploadDir() {
        return uploadDir;
    }
}
