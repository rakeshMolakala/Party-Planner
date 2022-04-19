package com.example.partyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SentAdapter extends RecyclerView.Adapter<SentAdapter.ViewHolder>{

    private List<SentItem> sentList;
    private Context context;

    public SentAdapter(List<SentItem> sentList, Context context) {
        this.sentList = sentList;
        this.context = context;
    }

    @NonNull
    @Override
    public SentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sent_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SentAdapter.ViewHolder holder, int position) {
        SentItem item = sentList.get(position);
        holder.sentName.setText(item.getSentName());
        holder.sentVenue.setText(item.getSentVenue());
        holder.sentTime.setText(item.getSentTime());
    }

    @Override
    public int getItemCount() {
        return sentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sentName;
        public TextView sentVenue;
        public TextView sentTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sentName = (TextView) itemView.findViewById(R.id.sentEventName);
            sentVenue = (TextView) itemView.findViewById(R.id.sentVenue);
            sentTime = (TextView) itemView.findViewById(R.id.sentEventTime);
        }
    }

}
