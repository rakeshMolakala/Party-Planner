package com.example.partyplanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.drinkItem.setText(drinks.get(position));

        holder.eachItemBox.setOnClickListener(view -> {
            Dialog d = new Dialog(context);
            d.setContentView(R.layout.activity_add);
            TextView title = d.findViewById(R.id.textView6);
            EditText drink = d.findViewById(R.id.item);
            Button addDrink = d.findViewById(R.id.buttonAdd);
            Button cancelDrink = d.findViewById(R.id.buttonCancel);
            title.setText("Update Drink");
            addDrink.setText("Update");
            drink.setText(drinks.get(position));
            String prev = drink.getText().toString().trim();
            addDrink.setOnClickListener(view1 -> {
                String itemName = drink.getText().toString().trim();
                if (drink.getText().toString().equals("")) {
                    Toast.makeText(context, "Enter details"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth authentication = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = authentication.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                    String userId = firebaseUser.getUid();
                    reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            List<List<String>> completePref = user.preferences;
                            completePref.get(1).remove(position);
                            completePref.get(1).add(itemName);
                            snapshot.getRef().child("preferences").setValue(completePref);
                            drinks = user.preferences.get(1);
                            notifyItemChanged(position);
                            d.dismiss();
                            Toast.makeText(context, prev + " has been updated!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            cancelDrink.setOnClickListener(view1 -> d.dismiss());
            d.show();
        });
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public void undo(String item, int position) {
        drinks.add(position, item);
        notifyItemInserted(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView drinkItem;
        LinearLayout eachItemBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            drinkItem = itemView.findViewById(R.id.eachItem);
            eachItemBox = itemView.findViewById(R.id.eachItemBox);
        }
    }
}