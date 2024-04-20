package com.amdrejr.phrases.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;

import com.amdrejr.phrases.entities.Phrase;
import com.amdrejr.phrases.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO implements Serializable {
    private Long id;
    private String username;
    @JsonIgnore
    private List<Map<String, Object>> allFollowers;
    @JsonIgnore
    private List<Map<String, Object>> allFollowing;

    public UserDTO() { }

    public UserDTO(Long id, String username, List<Phrase> phrases) {
        this.id = id;
        this.username = username;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
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

    public List<Map<String, Object>> getAllFollowers() {
        return allFollowers;
    }

    public void setAllFollowers(List<Map<String, Object>> allFollowers) {
        this.allFollowers = allFollowers;
    }

    public List<Map<String, Object>> getAllFollowing() {
        return allFollowing;
    }

    public void setAllFollowing(List<Map<String, Object>> allFollowing) {
        this.allFollowing = allFollowing;
    }

    public Integer getTotalFollowers() {
        return allFollowers.size();
    }

    public boolean getIsIFollowing() {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Long> followersIds = allFollowers.stream().map(follower -> (Long) follower.get("id")).toList();

        return followersIds.contains(actualUser.getId());
    }

    public Integer getTotalFollowing() {
        return allFollowing.size();
    }
}