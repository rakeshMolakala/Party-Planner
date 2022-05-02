package com.example.partyplanner;

import java.util.List;

public class Event {
    public String cardID;
    public String host;
    public List<String> invitees;
    public String name;
    public String time;
    public String venue;

    public Event() {

    }

    public Event(String cardID, String host, List<String> invitees, String name, String time, String venue) {
        this.cardID = cardID;
        this.host = host;
        this.invitees = invitees;
        this.name = name;
        this.time = time;
        this.venue = venue;
    }
}
