package com.hackerearth.fullstack.backend.payload.response;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventResponse {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public int id;
    public String type;
    public int repoId;
    public int actorId;
    public LocalDateTime timestamp;

    @JsonProperty("public")
    public boolean isPublic;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        EventResponse that = (EventResponse) obj;
        return actorId == that.actorId
                && repoId == that.repoId
                && isPublic == that.isPublic
                && (type != null ? type.equals(that.type) : that.type == null);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + repoId;
        result = 31 * result + actorId;
        result = 31 * result + (isPublic ? 1 : 0);
        return result;
    }
}
