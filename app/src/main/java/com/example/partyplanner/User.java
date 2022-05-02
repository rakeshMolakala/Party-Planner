package com.example.partyplanner;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class User {

    public String username, email, number;
    public List<String> requestsSent, requestsReceived, address;
    public List<List<String>> preferences;
    public Map<String, List<String>> friendsList;
    public String profileImage;

    public User() {

    }

    public User(String username, String email, String number, List<String> address,
                List<String> requestsReceived, List<String> requestsSent, List<List<String>>
                        preferences, Map<String, List<String>> friendsList, String profileImage) {
        this.username = username;
        this.email = email;
        this.number = number;
        this.address = address;
        this.requestsReceived = requestsReceived;
        this.requestsSent = requestsSent;
        this.preferences = preferences;
        this.friendsList = friendsList;
        this.profileImage = profileImage;
    }

    @NonNull
    @Override
    public String toString() {
        return this.username + " " + this.email + " " + this.profileImage;
    }
}