package com.example.partyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder>{

    private List<RecItem> recList;
    private Context context;

    public RecAdapter(List<RecItem> recList, Context context) {
        this.recList = recList;
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return recList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recName;
        public TextView recVenue;
        public TextView recTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recName = (TextView) itemView.findViewById(R.id.recEventName);
            recVenue = (TextView) itemView.findViewById(R.id.recEventVenue);
            recTime = (TextView) itemView.findViewById(R.id.recEventTime);
        }
    }
}
