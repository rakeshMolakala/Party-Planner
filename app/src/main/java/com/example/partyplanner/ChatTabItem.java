package com.example.partyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ChatTabItem extends AppCompatActivity implements ChatItemListener {

    private final String userName;
    private final String userEmail;

    public ChatTabItem(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserName() {
        return this.userName;
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