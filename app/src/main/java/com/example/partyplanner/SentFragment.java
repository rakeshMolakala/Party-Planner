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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentFragment extends Fragment {

    ArrayList<SentItem> sentList;
    SentAdapter sentAdapter;
    Map<String ,List<String>> inviteesMap;
    Map<Integer,String> inviteesMap2;
    int  c = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sentList = new ArrayList<>();
        inviteesMap =  new HashMap<>();
        inviteesMap2 =  new HashMap<>();
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
                String eventName = snapshot.getKey();
                String host = snapshot.child("host").getValue().toString();


                Object invitees = snapshot.child("invitees").getValue();
                inviteesMap.put(eventName,(List<String>) invitees);
                inviteesMap2.put(c,eventName);
                c++;
                if(host.equals(firebaseUserEmail)){
                    Log.d("100",firebaseUserEmail);
                    String name = snapshot.child("name").getValue().toString();
                    String venue = snapshot.child("venue").getValue().toString();
                    String time = snapshot.child("time").getValue().toString();
                    sentList.add(new SentItem(name,venue,time));
                }

                Log.d("1234Map",inviteesMap.toString());
                Log.d("1234EventName",eventName);

                sentAdapter = new SentAdapter(sentList, getContext(),inviteesMap,inviteesMap2);
                recyclerView.setAdapter(sentAdapter);

                if(sentList.size()>0){
                    Log.d("200",""+sentAdapter.getEditId(0));
                }

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
