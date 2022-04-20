package com.example.partyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerFoodAdapter extends RecyclerView.Adapter<RecyclerFoodAdapter.MyViewHolder> {

    Context context;
    List<String> foods;

    public RecyclerFoodAdapter(Context context, List<String> foods) {
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.foodItem.setText(foods.get(position));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView foodItem;
        LinearLayout eachItemBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodItem = itemView.findViewById(R.id.eachItem);
            eachItemBox = itemView.findViewById(R.id.eachItemBox);
        }
    }

    public void delete(int position){
        foods.remove(position);
        notifyItemRemoved(position);
    }

    public void undo(String item, int position) {
        foods.add(position, item);
        notifyItemInserted(position);
    }
}