package com.example.partyplanner;

public class TodoModel {
    boolean isChecked;
    String text;
    String key;

    public TodoModel() {
    }

    public TodoModel(boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
