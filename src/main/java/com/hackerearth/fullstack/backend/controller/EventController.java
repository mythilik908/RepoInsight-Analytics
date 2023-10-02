package com.hackerearth.fullstack.backend.controller;

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

import com.hackerearth.fullstack.backend.exception.CustomException;
import com.hackerearth.fullstack.backend.model.Event;
import com.hackerearth.fullstack.backend.model.Repo;
import com.hackerearth.fullstack.backend.payload.request.EventRequest;
import com.hackerearth.fullstack.backend.payload.response.EventResponse;
import com.hackerearth.fullstack.backend.repository.EventRepository;
import com.hackerearth.fullstack.backend.repository.RepoRepository;
import com.hackerearth.fullstack.backend.utils.Constants;

@RestController
@RequestMapping("api/v1")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    RepoRepository repoRepository;
 
    @PostMapping("/events")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest){
        Optional<Repo> repoContainer=repoRepository.findById(eventRequest.getRepoId());
        Repo repo;
        if(!repoContainer.isPresent())
        {
            repo=new Repo();
            repo.setId(eventRequest.getRepoId());
            repo=repoRepository.save(repo);
        }
        else{
            repo=repoContainer.get();
        }
        Event event=new Event();
        List<Event> list=new ArrayList<>();
        list.addAll(repo.getEvents());
        int i =0 ;
        event.setId(i++);
        event.setActorId(eventRequest.getActorId());
        event.setPublic(eventRequest.isPublic());
        event.setType(eventRequest.getType());
        event.setRepo(repo);
        list.add(event);
        repo.setEvents(list);
        System.out.println(repo.getEvents().size());
        //Your Code here
     
        event=eventRepository.save(event);
        EventResponse eventResponse=new EventResponse();
        eventResponse.setId((int)event.getId());
               eventResponse.setActorId((int)eventRequest.getActorId());
               eventResponse.setType(eventRequest.getType());
               eventResponse.setPublic(eventRequest.isPublic());
               eventResponse.setRepoId((int)eventRequest.getRepoId());
        //Your Code here
        return ResponseEntity.status(Constants.EVENT_CREATED).body(eventResponse);
    }
    @GetMapping("/events")
    public ResponseEntity<Iterable<EventResponse>> getAllEvents(){
        List<EventResponse> list=new ArrayList<>();
        for(Event e:eventRepository.findAll()){
            EventResponse eventResponse=new EventResponse();
            eventResponse.setActorId((int)e.getActorId());
            eventResponse.setRepoId((int)e.getRepo().getId());
            eventResponse.setId((int)e.getId());
            eventResponse.setType(e.getType());
            eventResponse.setPublic(e.isPublic());
            list.add(eventResponse);
        }
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(list);
    }

    @GetMapping("/repos/{repoId}/events")
    public ResponseEntity<Iterable<EventResponse>> getEventsForRepo(@PathVariable long repoId)throws CustomException
    {
        List<EventResponse> list=new ArrayList<>();
        Optional<Repo> optionalRepo=repoRepository.findById(repoId);
        if(!optionalRepo.isPresent()){
            throw new CustomException(Constants.NO_SUCH_REPO_MESSAGE,Constants.NO_SUCH_REPO);
        }
        for(Event e:repoRepository.findById(repoId).get().getEvents()){
            EventResponse eventResponse=new EventResponse();
            eventResponse.setActorId((int)e.getActorId());
            eventResponse.setRepoId((int)e.getRepo().getId());
            eventResponse.setId((int)e.getId());
            eventResponse.setType(e.getType());
            eventResponse.setPublic(e.isPublic());
            list.add(eventResponse);
        }
        return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(list);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable long eventId)throws CustomException
    {
        Optional<Event> eventContainer=eventRepository.findById(eventId);
        if(!eventContainer.isPresent())
            throw new CustomException(Constants.NO_SUCH_EVENT_MESSAGE,Constants.NO_SUCH_EVENT);
        Event e=eventContainer.get();
        EventResponse eventResponse=new EventResponse();
        eventResponse.setActorId((int)e.getActorId());
        eventResponse.setRepoId((int)e.getRepo().getId());
        eventResponse.setId((int)e.getId());
        eventResponse.setType(e.getType());
        eventResponse.setPublic(e.isPublic());
       return ResponseEntity.status(Constants.RESPONSE_GENERATED).body(eventResponse);
    }

}
