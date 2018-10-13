package ru.saidgadjiev.aboutme;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.saidgadjiev.aboutme.properties.DataSourceProperties;
import ru.saidgadjiev.aboutme.properties.StorageProperties;
import ru.saidgadjiev.aboutme.storage.StorageService;

/**
 * Created by said on 12.02.2018.
 */
@EnableConfigurationProperties(value = {
        StorageProperties.class,
        DataSourceProperties.class
})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Bean
    CommandLineRunner init(@Qualifier("resourceStorageService") StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }
}
