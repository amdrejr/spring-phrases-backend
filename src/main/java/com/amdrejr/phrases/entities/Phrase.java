package com.amdrejr.phrases.entities;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "phrases")
public class Phrase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JsonIgnore private User user;
    private String text;
    private Date date;
    @ManyToMany
    @JsonIgnore
    private Set<User> usersLiked = new HashSet<>();

    public Phrase() { }

    public Phrase(Long id, User user, String text, Date date) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.date = date;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getLikes() {
        return usersLiked.size();
    }

    public void addLikeByUserId(User user) {
        this.usersLiked.add(user);
    }
    
    public void removeLikeByUserId() {
        this.usersLiked.remove(user);
    }

    public String getAuthor() {
        return user.getUsername();
    }

    public Set<User> getUsersLiked() {
        return usersLiked;
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

    // public void setUsersLiked(Set<Long> usersLiked) {
    //     this.usersLiked = usersLiked;
    // }


}
