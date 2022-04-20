package com.example.partyplanner;

public class InviteItem {

    private String name;
    private String email;
    private boolean checked;

    public InviteItem(String name, String email){
        this.checked = false;
        this.name=name;
        this.email=email;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public boolean getChecked(){
        return this.checked;
    }

    public void setChecked(boolean newCheck){
        this.checked = newCheck;
    }
}
