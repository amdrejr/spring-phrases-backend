package com.amdrejr.phrases.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Map<String, Object>> likedByUsers = new ArrayList<>();

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
        this.likes = phrase.getLikedByUsers().size();
        this.likedByUsers = phrase.getUsersLiked();
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

    public List<Map<String, Object>> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(List<Map<String, Object>> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }
    
}
