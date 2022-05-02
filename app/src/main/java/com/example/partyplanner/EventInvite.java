package com.example.partyplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventInvite extends AppCompatActivity {

    TextView addressDisplay;
    List<String> invitees;
    long eventCount;
    String cardID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event_invite);

        Intent i = getIntent();
        String eventType = i.getStringExtra("event");
        String invitationCard = i.getStringExtra("backgroundImage");

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();

        ConstraintLayout constraintLayout = findViewById(R.id.eventBackground);

        if (eventType.equals("birthday") && invitationCard.equals("1")) {
            constraintLayout.setBackgroundResource(R.drawable.birthday1);
            cardID = "b1";
        } else if (eventType.equals("birthday") && invitationCard.equals("2")) {
            constraintLayout.setBackgroundResource(R.drawable.birthday2);
            cardID = "b2";
        } else if (eventType.equals("wedding") && invitationCard.equals("1")) {
            constraintLayout.setBackgroundResource(R.drawable.wedding1);
            cardID = "w1";
        } else if (eventType.equals("wedding") && invitationCard.equals("2")) {
            constraintLayout.setBackgroundResource(R.drawable.wedding2);
            cardID = "w2";
        } else if (eventType.equals("pizza") && invitationCard.equals("1")) {
            constraintLayout.setBackgroundResource(R.drawable.pizza1);
            cardID = "p1";
        } else if (eventType.equals("pizza") && invitationCard.equals("2")) {
            constraintLayout.setBackgroundResource(R.drawable.pizza2);
            cardID = "p2";
        } else if (eventType.equals("christmas") && invitationCard.equals("1")) {
            constraintLayout.setBackgroundResource(R.drawable.christmas1);
            cardID = "c1";
        } else if (eventType.equals("christmas") && invitationCard.equals("2")) {
            constraintLayout.setBackgroundResource(R.drawable.christmas2);
            cardID = "c2";
        } else if (eventType.equals("halloween") && invitationCard.equals("1")) {
            constraintLayout.setBackgroundResource(R.drawable.halloween1);
            cardID = "h1";
        } else if (eventType.equals("halloween") && invitationCard.equals("2")) {
            constraintLayout.setBackgroundResource(R.drawable.halloween2);
            cardID = "h2";
        } else if (eventType.equals("dance") && invitationCard.equals("1")) {
            constraintLayout.setBackgroundResource(R.drawable.dance1);
            cardID = "d1";
        } else if (eventType.equals("dance") && invitationCard.equals("2")) {
            constraintLayout.setBackgroundResource(R.drawable.dance2);
            cardID = "d2";
        }

        TextInputLayout eventNameHolder = findViewById(R.id.eventNameHolder);
        TextInputLayout dateHolder = findViewById(R.id.dateHolder);
        TextInputEditText datePick = findViewById(R.id.date);
        TextInputLayout timeHolder = findViewById(R.id.timeHolder);
        TextInputEditText timePick = findViewById(R.id.time);
        Button dateButton = findViewById(R.id.dateButton);
        Button timeButton = findViewById(R.id.timeButton);
        addressDisplay = findViewById(R.id.addressDisplay);
        Button addressButton = findViewById(R.id.addressButton);
        Button createEvent = findViewById(R.id.createEvent);
        invitees = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    eventCount = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createEvent.setOnClickListener(view -> {
            if (eventNameHolder.getEditText().getText().toString().isEmpty()) {
                eventNameHolder.setError("Event name is required!");
                eventNameHolder.requestFocus();
                return;
            } else {
                eventNameHolder.setError(null);
            }
            if (dateHolder.getEditText().getText().toString().isEmpty()) {
                dateHolder.setError("Date is required!");
                dateHolder.requestFocus();
                return;
            } else {
                dateHolder.setError(null);
            }
            if (timeHolder.getEditText().getText().toString().isEmpty()) {
                timeHolder.setError("Time is required!");
                timeHolder.requestFocus();
                return;
            } else {
                timeHolder.setError(null);
            }

            invitees.add("sushithreddy21@gmail.com");
            if (firebaseUser != null) {
                Event event = new Event(cardID, firebaseUser.getEmail(), invitees, eventNameHolder.getEditText().getText().toString().trim(), dateHolder.getEditText().getText().toString().trim() + " " + timeHolder.getEditText().getText().toString().trim(), addressDisplay.getText().toString().trim());
                reference.child(String.valueOf(eventCount + 1)).setValue(event).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EventInvite.this, "Event has been created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EventInvite.this, EventActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
            }
        });

        dateButton.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, date1) -> {
                String dateString = month1 + "/" + date1 + "/" + year1;
                datePick.setText(dateString);
            }, year, month, date);
            datePickerDialog.show();
        });

        timeButton.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, minute1) -> {
                String timeString = "at " + hour1 + ":" + minute1;
                timePick.setText(timeString);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        addressButton.setOnClickListener(view -> {

        });
    }
}
