package com.example.partyplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewProfile extends AppCompatActivity {

    String userName;
    ImageView profilePictureView;
    TextView accountNameView;
    TextView eventConductedCountView, eventAttendedCountView, emailView, phoneNumberView, addressLine1View, addressLine2View, addressLine3View, preferencesView;
    ProgressBar progressbarProfileView;
    ListView foodsView, drinksView;
    LinearLayout addressMaps;
    String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_viewprofile);

        Intent i = getIntent();
        userName = i.getStringExtra("UserName");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();

        profilePictureView = findViewById(R.id.profilePictureView);
        accountNameView = findViewById(R.id.accountNameView);
        eventConductedCountView = findViewById(R.id.eventConductedCountView);
        eventAttendedCountView = findViewById(R.id.eventAttendedCountView);
        emailView = findViewById(R.id.emailView);
        phoneNumberView = findViewById(R.id.phoneNumberView);
        addressLine1View = findViewById(R.id.addressLine1View);
        addressLine2View = findViewById(R.id.addressLine2View);
        addressLine3View = findViewById(R.id.addressLine3View);
        progressbarProfileView = findViewById(R.id.progressbarProfileView);
        foodsView = findViewById(R.id.foodsView);
        drinksView = findViewById(R.id.drinksView);
        addressMaps = findViewById(R.id.addressMaps);

        progressbarProfileView.setVisibility(View.VISIBLE);
        if (firebaseUser == null) {
            Toast.makeText(ViewProfile.this, "Something went wrong! Credentials are not available at the moment", Toast.LENGTH_LONG).show();
            progressbarProfileView.setVisibility(View.GONE);
        } else {
            FirebaseDatabase.getInstance().getReference("Users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    User details = dataSnapshot.getValue(User.class);
                    if (details != null) {
                        String name = details.username;
                        if (name.equals(userName)) {
                            if (details.profileImage.length() == 5) {
                                profilePictureView.setImageResource(R.drawable.user);
                            } else {
                                Picasso.with(ViewProfile.this).load(Uri.parse(details.profileImage)).into(profilePictureView);
                            }
                            accountNameView.setText(details.username);
                            emailView.setText(details.email);
                            phoneNumberView.setText(details.number);
                            addressLine1View.setText(details.address.get(0));
                            addressLine2View.setText(details.address.get(1));
                            addressLine3View.setText(details.address.get(2));
                            address = addressLine1View.getText().toString() + ", " + addressLine2View.getText().toString() + ", " + addressLine3View.getText().toString();
                            ArrayList<String> foodsArray = new ArrayList<>(details.preferences.get(0));
                            ArrayList<String> drinksArray = new ArrayList<>(details.preferences.get(1));
                            ArrayAdapter<String> foodsAdapter = new ArrayAdapter<>(ViewProfile.this, android.R.layout.simple_list_item_1, foodsArray);
                            ArrayAdapter<String> drinksAdapter = new ArrayAdapter<>(ViewProfile.this, android.R.layout.simple_list_item_1, drinksArray);
                            foodsView.setAdapter(foodsAdapter);
                            drinksView.setAdapter(drinksAdapter);
                        }
                    } else {
                        Toast.makeText(ViewProfile.this, "User not found!!", Toast.LENGTH_LONG).show();
                    }
                    progressbarProfileView.setVisibility(View.GONE);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        emailView.setOnClickListener(view -> {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("message/rfc822");
            startActivity(myIntent);
        });

        phoneNumberView.setOnClickListener(view -> {
            Intent myIntent = new Intent(Intent.ACTION_DIAL);
            String phNum = "tel:" + phoneNumberView.getText();
            myIntent.setData(Uri.parse(phNum));
            startActivity(myIntent);
        });

        addressMaps.setOnClickListener(view -> {
            if (address.length()>5) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}