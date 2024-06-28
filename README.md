# Event Management System

This project is an event management system developed using Spring Boot. The system provides RESTful APIs for creating, retrieving, and managing events and repositories. 
Each event is associated with a repository, and the system supports various operations such as creating new events, listing all events, retrieving events for a specific repository, and getting details of a specific event.

## Features
Allows the creation of a new event associated with a repository. If the repository does not exist, it creates a new one.
Retrieves a list of all events.
Retrieves all events associated with a specific repository.
Retrieves details of a specific event by its ID.

## Tech Stack

Backend:
Spring Boot: Framework for building RESTful web services in Java.
Spring Data JPA: For database operations.
H2 Database: In-memory database for development and testing.
Languages:
Java: Primary programming language for backend development.
Build Tool:
Maven: For project management and dependency management.
