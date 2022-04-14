package com.example.partyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class RequestListItem extends AppCompatActivity implements RequestItemListener {

    private final String userName;

    public RequestListItem(String userName) {
        this.userName = userName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list_item);
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public void onRequestItemClick(int position, String userName, Context context) {

    }
}