# Repo Insight Analytics System

<p align="center">
  <img src="https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue" alt="Version">
  <img src="https://img.shields.io/badge/java-8+-orange" alt="Java version">
  <img src="https://img.shields.io/badge/spring--boot-2.7.18-brightgreen" alt="Spring Boot version">
  <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
</p>

A Spring Boot service for capturing and analyzing repository events to help teams monitor activity, uncover workflow patterns, and spot bottlenecks through actionable metrics.

## ✨ Features

- **📊 Event Tracking**  
  Create and fetch events tied to repositories with comprehensive metadata.
  
- **📈 Workflow Analytics**  
  Discover frequent event sequences and patterns to optimize development processes.
  
- **🔍 Bottleneck Detection**  
  Identify slowdowns via transition‑time analysis for improved efficiency.
  
- **📉 Activity Trends**  
  Generate time‑series metrics and visual summaries of repository activity.
  
- **⚙️ Configurable**  
  Externalized settings for easy customization and extensions.

## 🧰 Tech Stack

- **Language & Framework:** Java 8+, Spring Boot 2.7.x
- **Data Access:** Spring Data JPA + SQLite
- **API:** RESTful endpoints, Jackson for JSON
- **Documentation:** SpringDoc OpenAPI
- **Build Tool:** Maven
- **Development:** Lombok, Spring DevTools
- **Quality Assurance:** JUnit, CheckStyle

## 📡 API Endpoints

### Event Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/events` | Create a new event |
| GET | `/api/v1/events` | List all events |
| GET | `/api/v1/events/{id}` | Get event details |
| GET | `/api/v1/repos/{id}/events` | Get all events for a repository |

### Analytics

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/analytics/activity-trends` | Fetch repository activity metrics |
| GET | `/api/v1/analytics/workflow-patterns` | Analyze common workflow sequences |
| GET | `/api/v1/analytics/bottlenecks` | Detect process slowdowns and stalled work |

## 💼 Use Cases

- **Development Monitoring**  
  Keep real‑time tabs on team activity and code churn.
  
- **Progress Tracking**  
  Visualize milestones and throughput over time.
  
- **Workflow Optimization**  
  Pinpoint stages that consistently lag or stall.
  
- **CI/CD Insights**  
  Use data‑driven insights to streamline pipelines.
