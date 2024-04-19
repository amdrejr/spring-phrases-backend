package com.amdrejr.phrases.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.amdrejr.phrases.entities.Phrase;
import com.amdrejr.phrases.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO implements Serializable {
    private Long id;
    private String username;
    @JsonIgnore
    private List<Phrase> phrases;
    private List<Map<String, Object>> allFollowers;
    private List<Map<String, Object>> allFollowing;

    public UserDTO() { }

    public UserDTO(Long id, String username, List<Phrase> phrases) {
        this.id = id;
        this.username = username;
        this.phrases = phrases;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phrases = user.getPhrases();
        this.allFollowers = user.getAllFollowers();
        this.allFollowing = user.getAllFollowing();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases;
    }

    // TODO: paginar isso
    public List<Map<String, Object>> getAllFollowers() {
        return allFollowers;
    }

    public void setAllFollowers(List<Map<String, Object>> allFollowers) {
        this.allFollowers = allFollowers;
    }

    // TODO: paginar isso
    public List<Map<String, Object>> getAllFollowing() {
        return allFollowing;
    }

    public void setAllFollowing(List<Map<String, Object>> allFollowing) {
        this.allFollowing = allFollowing;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", username=" + username + ", phrases=" + phrases + ", allFollowers="
                + allFollowers + ", allFollowing=" + allFollowing + "]";
    }

}