package ru.saidgadjiev.aboutme.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class StorageProperties {

    @Value("${storage.location}")
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }
}
