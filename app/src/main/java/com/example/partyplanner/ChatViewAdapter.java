

package com.example.partyplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private final ArrayList<ChatTabItem> itemList;
    private ChatItemListener listener;

    public ChatViewAdapter(ArrayList<ChatTabItem> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(ChatItemListener listener) {
        this.listener = listener;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_tab_item, parent, false);
        return new ChatViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatTabItem currentItem = itemList.get(position);
        holder.userName.setText(currentItem.getUserName());
        holder.email.setText(currentItem.getUserEmail());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
