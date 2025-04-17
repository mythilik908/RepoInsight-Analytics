package com.hackerearth.fullstack.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-config.properties")
public class ApplicationConfig {

    @Value("${api.response.event-created}")
    private int eventCreated;

    @Value("${api.response.not-found}")
    private int notFound;

    @Value("${api.response.ok}")
    private int ok;

    @Value("${error.message.no-such-event}")
    private String noSuchEventMessage;

    @Value("${error.message.no-such-repo}")
    private String noSuchRepoMessage;

    @Value("${api.base-path}")
    private String apiBasePath;

    @Value("${api.events-path}")
    private String eventsPath;

    @Value("${api.repos-path}")
    private String reposPath;

    @Value("${analytics.default-days}")
    private int defaultAnalyticsDays;

    public int getEventCreated() {
        return eventCreated;
    }

    public int getNotFound() {
        return notFound;
    }

    public int getOk() {
        return ok;
    }

    public String getNoSuchEventMessage() {
        return noSuchEventMessage;
    }

    public String getNoSuchRepoMessage() {
        return noSuchRepoMessage;
    }

    public String getApiBasePath() {
        return apiBasePath;
    }

    public String getEventsPath() {
        return eventsPath;
    }

    public String getReposPath() {
        return reposPath;
    }

    public int getDefaultAnalyticsDays() {
        return defaultAnalyticsDays;
    }
}
