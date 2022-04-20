package com.example.partyplanner;

public class RecItem {

    private String recName;
    private String recVenue;
    private String recTime;

    public RecItem(String recName, String recVenue, String recTime){
        this.recName = recName;
        this.recVenue = recVenue;
        this.recTime = recTime;
    }

    public String getRecName(){
        return this.recName;
    }

    public String getRecVenue(){
        return this.recVenue;
    }

    public String getRecTime(){
        return this.recTime;
    }

}
