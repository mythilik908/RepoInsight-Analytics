package com.hackerearth.fullstack.backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hackerearth.fullstack.backend.config.ApplicationConfig;

@Component
public class Constants {

    private static ApplicationConfig applicationConfig;

    @Autowired
    public Constants(ApplicationConfig applicationConfig) {
        Constants.applicationConfig = applicationConfig;
    }

    // HTTP Status codes
    public static final int EVENT_CREATED = getEventCreated();
    public static final int NO_SUCH_EVENT = getNotFound();
    public static final int NO_SUCH_REPO = getNotFound();
    public static final int RESPONSE_GENERATED = getOk();

    // Error messages
    public static final String NO_SUCH_EVENT_MESSAGE = getNoSuchEventMessage();
    public static final String NO_SUCH_REPO_MESSAGE = getNoSuchRepoMessage();

    // Helper methods to get values from config
    private static int getEventCreated() {
        return applicationConfig != null ? applicationConfig.getEventCreated() : 201;
    }

    private static int getNotFound() {
        return applicationConfig != null ? applicationConfig.getNotFound() : 404;
    }

    private static int getOk() {
        return applicationConfig != null ? applicationConfig.getOk() : 200;
    }

    private static String getNoSuchEventMessage() {
        return applicationConfig != null ? applicationConfig.getNoSuchEventMessage() : "There is no such event";
    }

    private static String getNoSuchRepoMessage() {
        return applicationConfig != null ? applicationConfig.getNoSuchRepoMessage() : "There is no such repository";
    }
}
