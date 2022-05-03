package com.example.partyplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{

    private List<RecItem> recList;
    private Context context;
    private Map<Integer,String> eventMap;

    public RecAdapter(List<RecItem> recList, Context context, Map<Integer,String> eventMap ) {
        this.recList = recList;
        this.context = context;
        this.eventMap = eventMap;
    }

    @NonNull
    @Override
    public RecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.received_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapter.ViewHolder holder, int position) {
        RecItem item = recList.get(position);
        holder.recName.setText(item.getRecName());
        holder.recVenue.setText(item.getRecVenue());
        holder.recTime.setText(item.getRecTime());
        String eventName = eventMap.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ShowRecCard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle extras = new Bundle();
                extras.putString("eventName",eventName);
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return recList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recName;
        public TextView recVenue;
        public TextView recTime;
        public Button chat;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recName = (TextView) itemView.findViewById(R.id.recEventName);
            recVenue = (TextView) itemView.findViewById(R.id.recEventVenue);
            recTime = (TextView) itemView.findViewById(R.id.recEventTime);
            chat = itemView.findViewById(R.id.chat);
            view = itemView;
        }
    }
}
