package com.example.tangthucac.model;


public class Message {
    private String content;
    private String sender; // "user" or "bot"

    public Message(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }
}