package com.example.partyplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ChatTabItem extends AppCompatActivity implements ChatItemListener {

    private final String userName;
    private final String userEmail;
    private final String profilePhotoUrl;

    public ChatTabItem(String userName, String userEmail, String profilePhotoUrl) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getProfilePhotoUrl() {
        return this.profilePhotoUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tab_item);
    }

    @Override
    public void onChatItemClick(int position, String userName, String userEmail, Context context) {
        Intent i = new Intent(context, ChatWindowActivity.class);
        i.putExtra("receivingUserName", userName);
        i.putExtra("receivingUserEmail", userEmail);
        context.startActivity(i);
    }
}