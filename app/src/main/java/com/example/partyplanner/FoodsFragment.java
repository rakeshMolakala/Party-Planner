package com.example.partyplanner;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodsFragment extends Fragment {

    RecyclerView foodsRecycler;
    DatabaseReference reference;
    RecyclerFoodAdapter adapter;
    List<String> foods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_foods, container, false);

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();
        foodsRecycler = viewGroup.findViewById(R.id.foodsRecycler);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        foods = new ArrayList<>();
        FloatingActionButton fabFoods = viewGroup.findViewById(R.id.fabFoods);

        String userId = firebaseUser.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                adapter = new RecyclerFoodAdapter(getActivity(), user.preferences.get(0));
                foodsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                foodsRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabFoods.setOnClickListener(view -> {
            Dialog d = new Dialog(getActivity());
            d.setContentView(R.layout.activity_add);
            EditText item = d.findViewById(R.id.item);
            Button addItem = d.findViewById(R.id.buttonAdd);

            addItem.setOnClickListener(view1 -> {
                String itemName = item.getText().toString().trim();
                if (itemName.equals("")) {
                    Toast.makeText(getActivity(), "Enter item", Toast.LENGTH_SHORT).show();
                }

                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        List<List<String>> completePref = user.preferences;
                        completePref.get(0).add(itemName);
                        snapshot.getRef().child("preferences").setValue(completePref);
                        adapter = new RecyclerFoodAdapter(getActivity(), completePref.get(0));
                        foodsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        foodsRecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        d.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });
            d.show();
        });

        return viewGroup;
    }
}