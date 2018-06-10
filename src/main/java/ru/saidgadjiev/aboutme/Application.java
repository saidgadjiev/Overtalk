package ru.saidgadjiev.aboutme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ru.saidgadjiev.aboutme.storage.StorageProperties;
import ru.saidgadjiev.aboutme.storage.StorageService;

/**
 * Created by said on 12.02.2018.
 */
@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
