

package com.example.partyplanner;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private final ArrayList<ChatTabItem> itemList;
    private ChatItemListener listener;
    private Context fragContext;

    public ChatViewAdapter(ArrayList<ChatTabItem> itemList, Context context) {
        this.itemList = itemList;
        this.fragContext = context;
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
        String profileUrl = currentItem.getProfilePhotoUrl();
        if (profileUrl.length()>5) {
            Picasso.with(fragContext).load(Uri.parse(profileUrl)).into(holder.profilePicture);
        }
        if (profileUrl.length()==5) {
            holder.profilePicture.setImageResource(R.drawable.user);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
