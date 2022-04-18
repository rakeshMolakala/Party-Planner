package com.example.partyplanner;

import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;

    public RequestViewHolder(View itemView, final RequestItemListener listener) {
        super(itemView);
        userName = itemView.findViewById(R.id.user_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRequestItemClick(position, userName.getText().toString(), itemView.getContext());
                    }
                }
            }
        });
    }
}