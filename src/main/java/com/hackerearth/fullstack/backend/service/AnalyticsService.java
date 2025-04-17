package com.hackerearth.fullstack.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackerearth.fullstack.backend.model.Event;
import com.hackerearth.fullstack.backend.payload.response.AnalyticsResponse;
import com.hackerearth.fullstack.backend.repository.EventRepository;

@Service
public class AnalyticsService {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Get activity trends for repositories over time
     *
     * @param days Number of days to analyze
     * @return Analytics response with repository activity stats
     */
    public AnalyticsResponse getActivityTrends(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        Iterable<Event> events = eventRepository.findAll();

        Map<Long, Integer> repoActivityCount = new HashMap<>();
        Map<String, Integer> eventTypeCount = new HashMap<>();
        Map<String, Integer> dailyActivityCount = new HashMap<>();

        for (Event event : events) {
            LocalDateTime timestamp = event.getTimestamp();

            // Skip events outside our time range
            if (timestamp == null || timestamp.isBefore(startDate)) {
                continue;
            }

            // Count by repository
            Long repoId = event.getRepo().getId();
            repoActivityCount.put(repoId, repoActivityCount.getOrDefault(repoId, 0) + 1);

            // Count by event type
            String eventType = event.getType();
            eventTypeCount.put(eventType, eventTypeCount.getOrDefault(eventType, 0) + 1);

            // Count by day
            String day = timestamp.toLocalDate().toString();
            dailyActivityCount.put(day, dailyActivityCount.getOrDefault(day, 0) + 1);
        }

        // Find most active repositories
        List<Map.Entry<Long, Integer>> topRepos = repoActivityCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        return new AnalyticsResponse(
                topRepos,
                eventTypeCount,
                dailyActivityCount,
                calculateAvgEventsPerDay(dailyActivityCount, days)
        );
    }

    /**
     * Get workflow patterns based on event sequence analysis
     *
     * @return Analytics response with workflow insights
     */
    public Map<String, Object> getWorkflowPatterns() {
        Map<String, Object> workflowInsights = new HashMap<>();
        List<Event> allEvents = (List<Event>) eventRepository.findAll();
        
        // Group events by repository for sequence analysis
        Map<Long, List<Event>> eventsByRepo = allEvents.stream()
                .filter(e -> e.getTimestamp() != null)
                .collect(Collectors.groupingBy(e -> e.getRepo().getId()));
        
        // Identify common event sequences (e.g., create -> update -> close)
        Map<String, Integer> commonSequences = new HashMap<>();
        Map<Long, Double> avgTimeToCompletion = new HashMap<>();
        
        for (Map.Entry<Long, List<Event>> repoEntry : eventsByRepo.entrySet()) {
            Long repoId = repoEntry.getKey();
            List<Event> repoEvents = repoEntry.getValue();
            
            // Sort events by timestamp
            repoEvents.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));
            
            // Analyze event sequences (look at consecutive pairs/triplets of events)
            for (int i = 0; i < repoEvents.size() - 1; i++) {
                String currentType = repoEvents.get(i).getType();
                String nextType = repoEvents.get(i+1).getType();
                String sequence = currentType + "->" + nextType;
                
                commonSequences.put(sequence, commonSequences.getOrDefault(sequence, 0) + 1);
                
                // If we have a triplet, record that sequence too
                if (i < repoEvents.size() - 2) {
                    String nextNextType = repoEvents.get(i+2).getType();
                    String tripletSequence = currentType + "->" + nextType + "->" + nextNextType;
                    commonSequences.put(tripletSequence, commonSequences.getOrDefault(tripletSequence, 0) + 1);
                }
            }
            
            // Calculate average time between first and last event (if we have at least 2 events)
            if (repoEvents.size() >= 2) {
                LocalDateTime firstEventTime = repoEvents.get(0).getTimestamp();
                LocalDateTime lastEventTime = repoEvents.get(repoEvents.size() - 1).getTimestamp();
                
                // Calculate duration in hours
                double durationHours = java.time.Duration.between(firstEventTime, lastEventTime).toMinutes() / 60.0;
                avgTimeToCompletion.put(repoId, durationHours);
            }
        }
        
        // Find top 5 most common sequences
        List<Map.Entry<String, Integer>> topSequences = commonSequences.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
        
        // Calculate typical workflow patterns
        Map<String, Integer> eventInitiators = new HashMap<>();
        Map<String, Integer> eventCompletors = new HashMap<>();
        
        // Identify common starting and ending events
        for (Map.Entry<Long, List<Event>> repoEntry : eventsByRepo.entrySet()) {
            List<Event> repoEvents = repoEntry.getValue();
            if (!repoEvents.isEmpty()) {
                String firstEventType = repoEvents.get(0).getType();
                String lastEventType = repoEvents.get(repoEvents.size() - 1).getType();
                
                eventInitiators.put(firstEventType, eventInitiators.getOrDefault(firstEventType, 0) + 1);
                eventCompletors.put(lastEventType, eventCompletors.getOrDefault(lastEventType, 0) + 1);
            }
        }
        
        workflowInsights.put("commonEventSequences", topSequences);
        workflowInsights.put("avgTimeToCompletionByRepo", avgTimeToCompletion);
        workflowInsights.put("commonWorkflowInitiators", sortByValueDescending(eventInitiators, 3));
        workflowInsights.put("commonWorkflowCompletors", sortByValueDescending(eventCompletors, 3));
        
        return workflowInsights;
    }

    /**
     * Identify potential bottlenecks in development workflow
     *
     * @return Map of bottleneck indicators
     */
    public Map<String, Object> identifyBottlenecks() {
        Map<String, Object> bottlenecks = new HashMap<>();
        List<Event> allEvents = (List<Event>) eventRepository.findAll();
        
        // Group events by repository for bottleneck analysis
        Map<Long, List<Event>> eventsByRepo = allEvents.stream()
                .filter(e -> e.getTimestamp() != null)
                .collect(Collectors.groupingBy(e -> e.getRepo().getId()));
        
        // Calculate time gaps between consecutive events
        Map<String, List<Double>> transitionTimes = new HashMap<>();
        Map<Long, Map<String, Double>> slowestTransitionsByRepo = new HashMap<>();
        Map<Long, String> reposWithLongGaps = new HashMap<>();
        
        for (Map.Entry<Long, List<Event>> repoEntry : eventsByRepo.entrySet()) {
            Long repoId = repoEntry.getKey();
            List<Event> repoEvents = repoEntry.getValue();
            
            // Sort events by timestamp
            repoEvents.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));
            
            Map<String, Double> repoTransitions = new HashMap<>();
            Double longestGap = 0.0;
            String longestGapTransition = "";
            
            // Analyze time between consecutive events
            for (int i = 0; i < repoEvents.size() - 1; i++) {
                Event current = repoEvents.get(i);
                Event next = repoEvents.get(i+1);
                
                String transition = current.getType() + "->" + next.getType();
                double hoursBetween = java.time.Duration.between(current.getTimestamp(), next.getTimestamp()).toMinutes() / 60.0;
                
                // Record the transition time
                if (!transitionTimes.containsKey(transition)) {
                    transitionTimes.put(transition, new ArrayList<>());
                }
                transitionTimes.get(transition).add(hoursBetween);
                
                // Record the transitions for this specific repo
                repoTransitions.put(transition, hoursBetween);
                
                // Track longest gap for this repo
                if (hoursBetween > longestGap) {
                    longestGap = hoursBetween;
                    longestGapTransition = transition;
                }
            }
            
            // Store the slowest transition for each repo
            slowestTransitionsByRepo.put(repoId, repoTransitions);
            
            // Record repos with gaps longer than 24 hours
            if (longestGap > 24.0) {
                reposWithLongGaps.put(repoId, longestGapTransition + " (" + String.format("%.1f", longestGap) + " hours)");
            }
        }
        
        // Calculate average transition times
        Map<String, Double> avgTransitionTimes = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : transitionTimes.entrySet()) {
            String transition = entry.getKey();
            List<Double> times = entry.getValue();
            
            double avg = times.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            avgTransitionTimes.put(transition, avg);
        }
        
        // Find the slowest transitions overall (potential bottlenecks)
        List<Map.Entry<String, Double>> slowestTransitions = avgTransitionTimes.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
        
        // Identify stalled repositories (no activity in last 7 days)
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        Map<Long, LocalDateTime> stalledRepos = new HashMap<>();
        
        for (Map.Entry<Long, List<Event>> repoEntry : eventsByRepo.entrySet()) {
            Long repoId = repoEntry.getKey();
            List<Event> repoEvents = repoEntry.getValue();
            
            if (!repoEvents.isEmpty()) {
                // Find the most recent event
                LocalDateTime mostRecent = repoEvents.stream()
                        .map(Event::getTimestamp)
                        .max(LocalDateTime::compareTo)
                        .orElse(null);
                
                if (mostRecent != null && mostRecent.isBefore(oneWeekAgo)) {
                    stalledRepos.put(repoId, mostRecent);
                }
            }
        }
        
        bottlenecks.put("slowestWorkflowTransitions", slowestTransitions);
        bottlenecks.put("reposWithLongGaps", reposWithLongGaps);
        bottlenecks.put("stalledRepositories", stalledRepos);
        
        return bottlenecks;
    }
    
    /**
     * Helper method to sort a map by value in descending order and limit results
     */
    private <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortByValueDescending(Map<K, V> map, int limit) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private double calculateAvgEventsPerDay(Map<String, Integer> dailyActivity, int days) {
        int totalEvents = dailyActivity.values().stream().mapToInt(Integer::intValue).sum();
        return (double) totalEvents / Math.min(days, dailyActivity.size());
    }
}
