package com.example.partyplanner;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoHolder extends RecyclerView.ViewHolder {

    CheckBox checkBox;
    TextView textView;

    public TodoHolder(@NonNull View itemView, final TodoListener todoListener) {
        super(itemView);

        checkBox = itemView.findViewById(R.id.todoCheckBox);
        textView = itemView.findViewById(R.id.todoText);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todoListener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        todoListener.onCheckBoxClick(position, checkBox.isChecked());
                    }
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todoListener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        todoListener.onCheckBoxClick(position, checkBox.isChecked());
                    }
                }
            }
        });
    }
}
