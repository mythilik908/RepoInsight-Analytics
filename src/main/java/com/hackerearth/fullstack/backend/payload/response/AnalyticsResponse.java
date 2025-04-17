package com.hackerearth.fullstack.backend.payload.response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AnalyticsResponse {

    private List<Map.Entry<Long, Integer>> mostActiveRepositories;
    private Map<String, Integer> eventTypeDistribution;
    private Map<String, Integer> dailyActivityTrend;
    private double averageEventsPerDay;

    public AnalyticsResponse(List<Entry<Long, Integer>> mostActiveRepositories,
            Map<String, Integer> eventTypeDistribution,
            Map<String, Integer> dailyActivityTrend,
            double averageEventsPerDay) {
        this.mostActiveRepositories = mostActiveRepositories;
        this.eventTypeDistribution = eventTypeDistribution;
        this.dailyActivityTrend = dailyActivityTrend;
        this.averageEventsPerDay = averageEventsPerDay;
    }

    public List<Map.Entry<Long, Integer>> getMostActiveRepositories() {
        return mostActiveRepositories;
    }

    public void setMostActiveRepositories(List<Map.Entry<Long, Integer>> mostActiveRepositories) {
        this.mostActiveRepositories = mostActiveRepositories;
    }

    public Map<String, Integer> getEventTypeDistribution() {
        return eventTypeDistribution;
    }

    public void setEventTypeDistribution(Map<String, Integer> eventTypeDistribution) {
        this.eventTypeDistribution = eventTypeDistribution;
    }

    public Map<String, Integer> getDailyActivityTrend() {
        return dailyActivityTrend;
    }

    public void setDailyActivityTrend(Map<String, Integer> dailyActivityTrend) {
        this.dailyActivityTrend = dailyActivityTrend;
    }

    public double getAverageEventsPerDay() {
        return averageEventsPerDay;
    }

    public void setAverageEventsPerDay(double averageEventsPerDay) {
        this.averageEventsPerDay = averageEventsPerDay;
    }
}
