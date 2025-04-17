package com.hackerearth.fullstack.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackerearth.fullstack.backend.config.ApplicationConfig;
import com.hackerearth.fullstack.backend.payload.response.AnalyticsResponse;
import com.hackerearth.fullstack.backend.service.AnalyticsService;
import com.hackerearth.fullstack.backend.utils.Constants;

@RestController
@RequestMapping("${api.base-path}/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private ApplicationConfig appConfig;

    /**
     * Get activity trends for repositories over a given time period
     *
     * @param days Number of days to analyze (default from configuration)
     * @return Analytics data with repository activity statistics
     */
    @GetMapping("/activity-trends")
    public ResponseEntity<AnalyticsResponse> getActivityTrends(
            @RequestParam(value = "days", required = false) Integer days) {
        // Use the config value if days parameter is not provided
        int daysToAnalyze = days != null ? days : appConfig.getDefaultAnalyticsDays();
        AnalyticsResponse analytics = analyticsService.getActivityTrends(daysToAnalyze);
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(analytics);
    }

    /**
     * Get workflow patterns based on event sequence analysis
     *
     * @return Workflow insight data
     */
    @GetMapping("/workflow-patterns")
    public ResponseEntity<Map<String, Object>> getWorkflowPatterns() {
        Map<String, Object> workflowInsights = analyticsService.getWorkflowPatterns();
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(workflowInsights);
    }

    /**
     * Identify potential bottlenecks in development processes
     *
     * @return Bottleneck analysis data
     */
    @GetMapping("/bottlenecks")
    public ResponseEntity<Map<String, Object>> identifyBottlenecks() {
        Map<String, Object> bottlenecks = analyticsService.identifyBottlenecks();
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(bottlenecks);
    }
}
