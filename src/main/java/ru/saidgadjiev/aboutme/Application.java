package ru.saidgadjiev.aboutme;

import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        logger.debug("App(Test)");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }
}
