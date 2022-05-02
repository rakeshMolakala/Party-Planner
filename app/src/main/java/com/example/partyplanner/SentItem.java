package com.example.partyplanner;

public class SentItem {

    private String sentName;
    private String sentVenue;
    private String sentTime;
    private int editId;

    public SentItem(String sentName, String sentVenue, String sentTime){
        this.sentName = sentName;
        this.sentVenue = sentVenue;
        this.sentTime = sentTime;
        this.editId = 768;
    }

    public String getSentName(){
        return this.sentName;
    }

    public String getSentVenue(){
        return this.sentVenue;
    }

    public String getSentTime(){
        return this.sentTime;
    }

    public void setEditId(int editId) {
        this.editId = editId;
    }

    public int getEditId() {
        return this.editId;
    }

}
