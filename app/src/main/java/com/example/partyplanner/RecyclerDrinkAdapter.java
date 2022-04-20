package com.example.partyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerDrinkAdapter extends RecyclerView.Adapter<RecyclerDrinkAdapter.MyViewHolder> {

    Context context;
    List<String> drinks;

    public RecyclerDrinkAdapter(Context context, List<String> drinks) {
        this.context = context;
        this.drinks = drinks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.drinkItem.setText(drinks.get(position));
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView drinkItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            drinkItem = itemView.findViewById(R.id.eachItem);
        }
    }
}