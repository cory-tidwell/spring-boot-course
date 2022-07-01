package com.coffeecat.springbootcourse.model.dto;

import java.util.Date;

public class SimpleMessage {
    private String from;
    private String text;
    private Date sent;
    private Long fromUID;
    private Boolean isReply;

    //Constructors:
    public SimpleMessage() {

    }

    public SimpleMessage(String text) {
        this.text = text;
        this.sent = new Date();
    }

    public SimpleMessage(Date sent, Long fromUID, String from, String text) {
        this.text = text;
        this.from = from;
        this.sent = sent;
        this.fromUID = fromUID;
    }

    //GETTERS & SETTERS:
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Long getFromUID() {
        return fromUID;
    }

    public void setFromUID(Long fromUID) {
        this.fromUID = fromUID;
    }

    public Boolean getReply() {
        return isReply;
    }

    public void setReply(Boolean reply) {
        isReply = reply;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                ", sent=" + sent +
                ", fromUID=" + fromUID +
                ", isReply=" + isReply +
                '}';
    }
}
