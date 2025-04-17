package com.hackerearth.fullstack.backend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackerearth.fullstack.backend.config.ApplicationConfig;
import com.hackerearth.fullstack.backend.exception.CustomException;
import com.hackerearth.fullstack.backend.model.Event;
import com.hackerearth.fullstack.backend.model.Repo;
import com.hackerearth.fullstack.backend.payload.request.EventRequest;
import com.hackerearth.fullstack.backend.payload.response.EventResponse;
import com.hackerearth.fullstack.backend.repository.EventRepository;
import com.hackerearth.fullstack.backend.repository.RepoRepository;
import com.hackerearth.fullstack.backend.utils.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("${api.base-path}")
@Tag(name = "Event Management", description = "APIs for creating and retrieving events")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    RepoRepository repoRepository;

    @Autowired
    private ApplicationConfig appConfig;

    @Operation(summary = "Create a new event", description = "Creates a new event associated with a repository")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Event created successfully", 
                    content = @Content(schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("${api.events-path}")
    public ResponseEntity<EventResponse> createEvent(
            @Parameter(description = "Event data", required = true) 
            @RequestBody EventRequest eventRequest) {
        Optional<Repo> repoContainer = repoRepository.findById(eventRequest.getRepoId());
        Repo repo;
        if (!repoContainer.isPresent()) {
            repo = new Repo();
            repo.setId(eventRequest.getRepoId());
            repo = repoRepository.save(repo);
        } else {
            repo = repoContainer.get();
        }
        Event event = new Event();
        List<Event> list = new ArrayList<>();
        list.addAll(repo.getEvents());

        event.setActorId(eventRequest.getActorId());
        event.setPublic(eventRequest.isPublic());
        event.setType(eventRequest.getType());
        event.setRepo(repo);
        // Set timestamp to current time
        event.setTimestamp(LocalDateTime.now());

        list.add(event);
        repo.setEvents(list);
        System.out.println(repo.getEvents().size());

        event = eventRepository.save(event);
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId((int) event.getId());
        eventResponse.setActorId((int) eventRequest.getActorId());
        eventResponse.setType(eventRequest.getType());
        eventResponse.setPublic(eventRequest.isPublic());
        eventResponse.setRepoId((int) eventRequest.getRepoId());
        eventResponse.setTimestamp(event.getTimestamp());

        return ResponseEntity.status(Constants.EVENT_CREATED).body(eventResponse);
    }

    @Operation(summary = "Retrieve all events", description = "Fetches all events")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "404", description = "No events found")
    })
    @GetMapping("${api.events-path}")
    public ResponseEntity<Iterable<EventResponse>> getAllEvents() {
        List<EventResponse> list = new ArrayList<>();
        for (Event e : eventRepository.findAll()) {
            EventResponse eventResponse = new EventResponse();
            eventResponse.setActorId((int) e.getActorId());
            eventResponse.setRepoId((int) e.getRepo().getId());
            eventResponse.setId((int) e.getId());
            eventResponse.setType(e.getType());
            eventResponse.setPublic(e.isPublic());
            eventResponse.setTimestamp(e.getTimestamp());
            list.add(eventResponse);
        }
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(list);
    }

    @Operation(summary = "Retrieve events for a repository", description = "Fetches all events associated with a specific repository")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "404", description = "Repository not found")
    })
    @GetMapping("${api.repos-path}/{repoId}/events")
    public ResponseEntity<Iterable<EventResponse>> getEventsForRepo(
            @Parameter(description = "Repository ID", required = true) 
            @PathVariable long repoId) throws CustomException {
        List<EventResponse> list = new ArrayList<>();
        Optional<Repo> optionalRepo = repoRepository.findById(repoId);
        if (!optionalRepo.isPresent()) {
            throw new CustomException(Constants.NO_SUCH_REPO_MESSAGE, Constants.NO_SUCH_REPO);
        }
        for (Event e : repoRepository.findById(repoId).get().getEvents()) {
            EventResponse eventResponse = new EventResponse();
            eventResponse.setActorId((int) e.getActorId());
            eventResponse.setRepoId((int) e.getRepo().getId());
            eventResponse.setId((int) e.getId());
            eventResponse.setType(e.getType());
            eventResponse.setPublic(e.isPublic());
            eventResponse.setTimestamp(e.getTimestamp());
            list.add(eventResponse);
        }
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(list);
    }

    @Operation(summary = "Retrieve a specific event", description = "Fetches details of a specific event by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = EventResponse.class))),
        @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @GetMapping("${api.events-path}/{eventId}")
    public ResponseEntity<EventResponse> getEvent(
            @Parameter(description = "Event ID", required = true) 
            @PathVariable long eventId) throws CustomException {
        Optional<Event> eventContainer = eventRepository.findById(eventId);
        if (!eventContainer.isPresent()) {
            throw new CustomException(Constants.NO_SUCH_EVENT_MESSAGE, Constants.NO_SUCH_EVENT);
        }
        Event e = eventContainer.get();
        EventResponse eventResponse = new EventResponse();
        eventResponse.setActorId((int) e.getActorId());
        eventResponse.setRepoId((int) e.getRepo().getId());
        eventResponse.setId((int) e.getId());
        eventResponse.setType(e.getType());
        eventResponse.setPublic(e.isPublic());
        eventResponse.setTimestamp(e.getTimestamp());
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(eventResponse);
    }
}
