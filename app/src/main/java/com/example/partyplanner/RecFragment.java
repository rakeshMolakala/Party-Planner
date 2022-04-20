package com.example.partyplanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecFragment extends Fragment {

    ArrayList<RecItem> recList;
    RecAdapter recAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_rec, container, false);
        FirebaseAuth authentication = FirebaseAuth.getInstance();
        String firebaseUserEmail = authentication.getCurrentUser().getEmail();
        RecyclerView recyclerView = v.findViewById(R.id.recRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataSnapshot = reference.child("Events");
        dataSnapshot.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Object invitees = snapshot.child("invitees").getValue();
                List<String> inviteesList = (List<String>) invitees;
                for(String s: inviteesList){
                    if(s.equals(firebaseUserEmail)){
                        String name = snapshot.child("name").getValue().toString();
                        String venue = snapshot.child("venue").getValue().toString();
                        String time = snapshot.child("time").getValue().toString();
                        recList.add(new RecItem(name,venue,time));
                        break;
                    }
                }
                recAdapter = new RecAdapter(recList, getContext().getApplicationContext());
                recyclerView.setAdapter(recAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return v;
    }
}
