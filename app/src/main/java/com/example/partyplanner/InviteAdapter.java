package com.example.partyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {

    private List<InviteItem> invList;
    private Context context;

    public InviteAdapter(List<InviteItem> invList, Context context) {
        this.invList = invList;
        this.context = context;
    }

    @NonNull
    @Override
    public InviteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.invite_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteAdapter.ViewHolder holder, int position) {
        InviteItem item = invList.get(position);
        holder.invName.setText(item.getName());
        holder.invEmail.setText(item.getEmail());
        holder.checkBox.setChecked(item.getChecked());
    }

    @Override
    public int getItemCount() {
        return invList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView invName;
        public TextView invEmail;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            invName = (TextView) itemView.findViewById(R.id.invName);
            invEmail = (TextView) itemView.findViewById(R.id.invEmail);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

}
