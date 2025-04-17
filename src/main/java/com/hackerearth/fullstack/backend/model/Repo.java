package com.hackerearth.fullstack.backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Repo {

    @Id
    private long id;

    @OneToMany(mappedBy = "repo", fetch = FetchType.EAGER)
    private List<Event> events = new ArrayList<>();

    public Repo(long id, List<Event> events) {
        this.id = id;
        this.events = events;
    }

    public Repo() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
