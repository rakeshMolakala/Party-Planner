package com.example.partyplanner;

import java.util.Date;

public class ChatMessageModel {
    private String sender;
    private String receiver;
    private long messageTime;
    private String message;

    public ChatMessageModel(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.messageTime = new Date().getTime();
    }

    public ChatMessageModel() {

    }

    public String getMessage() {
        return this.message;
    }

    public String getSender() {
        return this.sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public long getMessageTime() {
        return this.messageTime;
    }



}
