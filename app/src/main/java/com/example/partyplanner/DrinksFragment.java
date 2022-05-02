package com.example.partyplanner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DrinksFragment extends Fragment {

    RecyclerView drinksRecycler;
    DatabaseReference reference;
    RecyclerDrinkAdapter adapter;
    List<String> drinks;
    FloatingActionButton fabDrinks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_drinks, container, false);

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();
        drinksRecycler = viewGroup.findViewById(R.id.drinksRecycler);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        drinks = new ArrayList<>();
        fabDrinks = viewGroup.findViewById(R.id.fabDrinks);

        String userId = firebaseUser.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                drinks = user.preferences.get(1);
                adapter = new RecyclerDrinkAdapter(getActivity(), user.preferences.get(1));
                drinksRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                drinksRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabDrinks.setOnClickListener(view -> {
            Dialog d = new Dialog(getActivity());
            d.setContentView(R.layout.activity_add);
            EditText item = d.findViewById(R.id.item);
            Button addItem = d.findViewById(R.id.buttonAdd);
            Button cancelItem = d.findViewById(R.id.buttonCancel);

            addItem.setOnClickListener(view1 -> {
                String itemName = item.getText().toString().trim();
                if (itemName.equals("")) {
                    Toast.makeText(getActivity(), "Enter item", Toast.LENGTH_SHORT).show();
                } else {
                    reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            List<List<String>> completePref = user.preferences;
                            completePref.get(1).add(itemName);
                            snapshot.getRef().child("preferences").setValue(completePref);
                            adapter = new RecyclerDrinkAdapter(getActivity(), completePref.get(1));
                            drinksRecycler.setAdapter(adapter);
                            d.dismiss();
                            Toast.makeText(DrinksFragment.this.getActivity(), itemName + " has been added to your drink preferences!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            cancelItem.setOnClickListener(view1 -> d.dismiss());
            d.show();
        });

        ItemTouchHelper.SimpleCallback swipe = new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView
                    , @NonNull RecyclerView.ViewHolder viewHolder
                    , @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        drinks = user.preferences.get(1);
                        String deletedDrink = drinks.get(viewHolder.getAdapterPosition());
                        int deletedDrinkIndex = viewHolder.getAdapterPosition();
                        List<List<String>> completePref = user.preferences;
                        completePref.get(1).remove(viewHolder.getAdapterPosition());
                        snapshot.getRef().child("preferences").setValue(completePref);
                        adapter = new RecyclerDrinkAdapter(getActivity(), completePref.get(1));
                        drinksRecycler.setAdapter(adapter);
                        final Snackbar snackbar = Snackbar
                                .make(drinksRecycler, deletedDrink + " removed", Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", view -> {
                            adapter.undo(deletedDrink, deletedDrinkIndex);
                            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    List<List<String>> completePref = user.preferences;
                                    completePref.get(1).add(deletedDrink);
                                    snapshot.getRef().child("preferences").setValue(completePref);
                                    adapter = new RecyclerDrinkAdapter(getActivity(), completePref.get(1));
                                    drinksRecycler.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        });
                        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.purple_500));
                        snackbar.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        new ItemTouchHelper(swipe).attachToRecyclerView(drinksRecycler);

        return viewGroup;
    }
}