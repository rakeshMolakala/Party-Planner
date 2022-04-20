package com.example.partyplanner;

import android.content.Context;

public interface ChatItemListener {
    void onChatItemClick(int position, String userName, String userEmail, Context context);
}
