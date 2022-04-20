package com.example.partyplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class SentFragment extends Fragment {

    ArrayList<SentItem> sentList;
    SentAdapter sentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sentList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_sent, container, false);
        FirebaseAuth authentication = FirebaseAuth.getInstance();
        String firebaseUserEmail = authentication.getCurrentUser().getEmail();
        RecyclerView recyclerView = v.findViewById(R.id.sentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataSnapshot = reference.child("Events");
        dataSnapshot.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String host = snapshot.child("host").getValue().toString();
                Log.d("100",firebaseUserEmail);
                Log.d("101",host);

//                Button inviteButton = v.findViewById(R.id.editButton);
//                inviteButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getActivity(), Invite.class);
//                        Bundle extras = new Bundle();
//                        Object invitees = snapshot.child("invitees").getValue();
//                        List<String> inviteesList = (List<String>) invitees;
//                        extras.putStringArrayList("invitees",(ArrayList<String>) inviteesList);
//                        intent.putExtras(extras);
//                        startActivity(intent);
//                    }
//                });
                if(host.equals(firebaseUserEmail)){
                    Log.d("100",firebaseUserEmail);
                    String name = snapshot.child("name").getValue().toString();
                    String venue = snapshot.child("venue").getValue().toString();
                    String time = snapshot.child("time").getValue().toString();
                    sentList.add(new SentItem(name,venue,time));
                }
                sentAdapter = new SentAdapter(sentList, getContext().getApplicationContext());
                recyclerView.setAdapter(sentAdapter);
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
