package com.example.partyplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoHolder> {

    private final ArrayList<TodoModel> todoModels;
    private TodoListener listener;

    public TodoAdapter(ArrayList<TodoModel> todoModels) {
        this.todoModels = todoModels;
    }

    public void setOnItemClickListener(TodoListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_items, parent, false);
        return new TodoHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
        TodoModel currentItem = todoModels.get(position);
        holder.checkBox.setChecked(currentItem.isChecked());
        holder.textView.setText(currentItem.getText());
    }

    @Override
    public int getItemCount() {
        return todoModels.size();
    }
}
