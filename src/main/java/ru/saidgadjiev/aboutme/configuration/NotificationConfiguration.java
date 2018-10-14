package ru.saidgadjiev.aboutme.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by said on 14.10.2018.
 */
@Configuration
@EnableScheduling
public class NotificationConfiguration {

    @Scheduled(fixedDelay = 1000)
    public void sendNotifications() {
        //TODO: send notifications
    }
}
