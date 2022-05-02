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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.foodItem.setText(foods.get(position));

        holder.eachItemBox.setOnClickListener(view -> {
            Dialog d = new Dialog(context);
            d.setContentView(R.layout.activity_add);
            TextView title = d.findViewById(R.id.textView6);
            EditText food = d.findViewById(R.id.item);
            Button addFood = d.findViewById(R.id.buttonAdd);
            Button cancelFood = d.findViewById(R.id.buttonCancel);
            title.setText("Update Food");
            addFood.setText("Update");
            food.setText(foods.get(position));
            String prev = food.getText().toString().trim();
            addFood.setOnClickListener(view1 -> {
                String itemName = food.getText().toString().trim();
                if (food.getText().toString().equals("")) {
                    Toast.makeText(context, "Enter details"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth authentication = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = authentication.getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                    if (firebaseUser != null) {
                        String userId = firebaseUser.getUid();
                        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                if (user != null) {
                                    List<List<String>> completePref = user.preferences;
                                    completePref.get(0).remove(position);
                                    completePref.get(0).add(itemName);
                                    snapshot.getRef().child("preferences").setValue(completePref);
                                    foods = user.preferences.get(0);
                                    notifyItemChanged(position);
                                    d.dismiss();
                                    Toast.makeText(context, prev + " has been updated!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "User not found!!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(context, "User not found!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            cancelFood.setOnClickListener(view1 -> d.dismiss());
            d.show();
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void undo(String item, int position) {
        foods.add(position, item);
        notifyItemInserted(position);
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
}