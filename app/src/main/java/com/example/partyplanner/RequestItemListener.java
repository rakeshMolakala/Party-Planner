package com.example.partyplanner;

import android.content.Context;

public interface RequestItemListener {
    void onRequestItemClick(int position, String userName, Context context);
}
