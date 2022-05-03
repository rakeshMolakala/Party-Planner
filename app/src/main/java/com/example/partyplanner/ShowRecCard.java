package com.example.partyplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowRecCard extends AppCompatActivity {


    private String currEvent;
    private TextView tName;
    private TextView tTime;
    private TextView tVenue;
    private TextView tHost;
    private RelativeLayout layout;
    private String hostEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rec_card);
        Bundle extras = getIntent().getExtras();
        currEvent = (String) extras.get("eventName");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataSnapshot = reference.child("Events");
        DatabaseReference dataSnapshotUsers = reference.child("Users");

        tName = findViewById(R.id.nameR);
        tVenue = findViewById(R.id.venueR);
        tTime = findViewById(R.id.timeR);
        tHost = findViewById(R.id.hostnameR);
        layout = findViewById(R.id.layoutIdR);

        dataSnapshot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String event = snapshot.getKey();
                if (event.equals(currEvent)) {
                    String name = snapshot.child("name").getValue().toString();
                    String time = snapshot.child("time").getValue().toString();
                    String venue = snapshot.child("venue").getValue().toString();
                    String image = snapshot.child("cardID").getValue().toString();
                    tName.setText(name);
                    tTime.setText(time);
                    tVenue.setText(venue);
                    if (image.charAt(0) == 'b' && image.charAt(1) == '1') {
                        layout.setBackgroundResource(R.drawable.birthday1);
                    } else if (image.charAt(0) == 'b' && image.charAt(1) == '2') {
                        layout.setBackgroundResource(R.drawable.birthday2);
                    } else if (image.charAt(0) == 'w' && image.charAt(1) == '1') {
                        layout.setBackgroundResource(R.drawable.wedding1);
                    } else if (image.charAt(0) == 'w' && image.charAt(1) == '2') {
                        layout.setBackgroundResource(R.drawable.wedding2);
                    } else if (image.charAt(0) == 'p' && image.charAt(1) == '1') {
                        layout.setBackgroundResource(R.drawable.pizza1);
                    } else if (image.charAt(0) == 'p' && image.charAt(1) == '2') {
                        layout.setBackgroundResource(R.drawable.pizza2);
                    } else if (image.charAt(0) == 'c' && image.charAt(1) == '1') {
                        layout.setBackgroundResource(R.drawable.christmas1);
                    } else if (image.charAt(0) == 'c' && image.charAt(1) == '2') {
                        layout.setBackgroundResource(R.drawable.christmas2);
                    } else if (image.charAt(0) == 'h' && image.charAt(1) == '1') {
                        layout.setBackgroundResource(R.drawable.halloween1);
                    } else if (image.charAt(0) == 'h' && image.charAt(1) == '2') {
                        layout.setBackgroundResource(R.drawable.halloween2);
                    } else if (image.charAt(0) == 'd' && image.charAt(1) == '1') {
                        layout.setBackgroundResource(R.drawable.dance1);
                    } else if (image.charAt(0) == 'd' && image.charAt(1) == '2') {
                        layout.setBackgroundResource(R.drawable.dance2);
                    }
                    hostEmail = snapshot.child("host").getValue().toString();
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

        dataSnapshotUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email = snapshot.child("email").getValue().toString();
                if (email.equals(hostEmail)) {
                    String hostName = snapshot.child("username").getValue().toString();
                    tHost.setText(hostName);
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


    }
}