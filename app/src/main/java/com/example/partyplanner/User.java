package com.example.partyplanner;

public class User {

    public String username, email, number, addressLine1, addressLine2, addressLine3;

    public User() {

    }

    public User(String username, String email, String number, String addressLine1, String addressLine2, String addressLine3) {
        this.username = username;
        this.email = email;
        this.number = number;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
    }
}
