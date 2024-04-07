package com.amdrejr.phrases.dto;

import java.util.List;

import com.amdrejr.phrases.entities.Phrase;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private List<Phrase> phrases;
    @JsonIgnore
    private Integer roleId;
    @JsonIgnore
    private boolean enabled;

    public UserDTO() { }

    public UserDTO(Long id, String username, List<Phrase> phrases) {
        this.id = id;
        this.username = username;
        this.phrases = phrases;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}