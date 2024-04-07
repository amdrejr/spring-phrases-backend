package com.amdrejr.phrases.dto;

public class PhraseDTO {
    private Long id;
    private Long userId;
    private String text;
    private String date;
    private Integer likes;

    public PhraseDTO() { }

    public PhraseDTO(Long id, Long userId, String text, String date, Integer likes) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.date = date;
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", userId='" + getUserId() + "'" +
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
