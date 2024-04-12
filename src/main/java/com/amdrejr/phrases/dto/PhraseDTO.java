package com.amdrejr.phrases.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amdrejr.phrases.entities.Phrase;
import com.amdrejr.phrases.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PhraseDTO implements Serializable {
    private Long id;
    @JsonIgnore
    private User user;
    private String text;
    private String date;
    private Integer likes;
    @JsonIgnore
    private Set<User> usersLiked = new HashSet<>();

    public PhraseDTO() { }

    public PhraseDTO(Long id, User user, String text, String date, Integer likes) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.date = date;
        this.likes = likes;
    }

    public PhraseDTO(Phrase phrase) {
        this.id = phrase.getId();
        this.user = phrase.getUser();
        this.text = phrase.getText();
        this.date = phrase.getDate().toString();
        this.likes = phrase.getUsersLiked().size();
        this.usersLiked = phrase.getUsersLiked();
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", userId='" + getUser() + "'" +
            ", text='" + getText() + "'" +
            ", date='" + getDate() + "'" +
            ", likes='" + getLikes() + "'" +
            "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Set<User> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(Set<User> usersLiked) {
        this.usersLiked = usersLiked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Object> getAuthor() {
        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("id", user.getId());
        mapa.put("username", user.getUsername());
        return mapa;
    }

    public List<Map<String, Object>> getAllUsersLiked() {
        // Evitar recursão infinita
        List<Map<String, Object>> usersLiked = new ArrayList<>();

        for (User user : this.usersLiked) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            usersLiked.add(userMap);
        }

        return usersLiked;
    }
}
