package com.example.partyplanner;

import java.util.List;
import java.util.Map;

public class User {

    public String username, email, number, addressLine1, addressLine2, addressLine3;
    public List<String> requestsSent;
    public List<String> requestsReceived;
    public Map<String, String> friendsList;

    public User() {

    }

    public User(String username, String email, String number, String addressLine1, String addressLine2,
                String addressLine3, List<String> requestsReceived, List<String> requestsSent,
                Map<String, String> friendsList) {
        this.username = username;
        this.email = email;
        this.number = number;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.requestsReceived = requestsReceived;
        this.requestsSent = requestsSent;
        this.friendsList = friendsList;
    }

    @Override
    public String toString() {
        return this.username + " " + this.email + " " + this.number + this.requestsSent + this.requestsReceived;
    }
}
