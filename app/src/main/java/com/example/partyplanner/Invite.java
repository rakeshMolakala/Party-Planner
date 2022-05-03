package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Invite extends AppCompatActivity {

    List<String> prevInv;
    String currEvent;
    ArrayList<InviteItem> invList;
    InviteAdapter invAdapter;
    ArrayList<InviteItem> checkedInvites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        invList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        prevInv = (List<String>) extras.get("invitees");
        currEvent = (String) extras.get("eventName");
        Set<String> prevSet = new HashSet<>(prevInv);

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        String firebaseUserEmail = authentication.getCurrentUser().getEmail();
        RecyclerView recyclerView = findViewById(R.id.inviteRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataSnapshot = reference.child("Users");

        dataSnapshot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String host = snapshot.child("email").getValue().toString();
                if (host.equals(firebaseUserEmail)) {
                    for (DataSnapshot o : snapshot.child("friendsList").getChildren()) {
                        String email = o.getKey();
                        email = email.replaceAll(",", ".");
                        String name = "";
                        for (DataSnapshot x : o.getChildren()) {
                            name = x.getValue().toString();
                            break;
                        }
                        if (!prevSet.contains(email) && !email.equals("dummy")) {
                            invList.add(new InviteItem(name, email));
                        }
                    }
                }
                invAdapter = new InviteAdapter(invList, getApplicationContext());
                recyclerView.setAdapter(invAdapter);
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

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        checkedInvites = new ArrayList<>();
        floatingActionButton.setOnClickListener(view -> {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference dataSnapshot1 = reference1.child("Events");
            ArrayList<String> newInvitees = new ArrayList<>();
            checkedInvites = invAdapter.getCheckedList();
            for (InviteItem inv : checkedInvites) {
                newInvitees.add(inv.getEmail());
            }

            dataSnapshot1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String event = snapshot.getKey();
                    if (event.equals(currEvent) && newInvitees.size() > 0) {
                        for (DataSnapshot o : snapshot.child("invitees").getChildren()) {
                            newInvitees.add(o.getValue().toString());
                        }
                        snapshot.getRef().child("invitees").setValue(newInvitees);
                        Toast.makeText(getApplicationContext(), "Invites sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        finish();
                        startActivity(intent);
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
        });
    }
}