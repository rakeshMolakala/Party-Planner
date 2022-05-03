package com.example.partyplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    private EditText eventName;
    private String currEvent;
    private DatabaseReference nameSnapshot;
    private DatabaseReference timeSnapshot;
    private String prevTime;
    private String prevDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Button ok = findViewById(R.id.ok);
        Button cancel = findViewById(R.id.cancel);
        eventName = findViewById(R.id.event_name);
        Bundle extras = getIntent().getExtras();
        currEvent = (String) extras.get("eventName");
        Button timeButton = findViewById(R.id.timeButton1);
        Button dateButton = findViewById(R.id.dateButton1);
        TextInputEditText datePick = findViewById(R.id.date);
        TextInputEditText timePick = findViewById(R.id.time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataSnapshot = reference.child("Events");

        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(intent);
        });

        dataSnapshot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String event = snapshot.getKey();
                if (event.equals(currEvent)) {
                    nameSnapshot = snapshot.getRef().child("name");
                    timeSnapshot = snapshot.getRef().child("time");
                    String x = snapshot.child("time").getValue().toString();
                    ;
                    String[] arr = x.split(" ");
                    prevDate = arr[0];
                    prevTime = arr[2];
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

        ok.setOnClickListener(view -> {
            String newEvent = eventName.getText().toString();
            if (newEvent.length() > 0) {
                nameSnapshot.setValue(newEvent);
                timeSnapshot.setValue(prevDate + " at " + prevTime);
                Toast.makeText(getApplicationContext(), "Details changed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        dateButton.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, date1) -> {
                String dateString = month1 + "/" + date1 + "/" + year1;
                datePick.setText(dateString);
                prevDate = datePick.getText().toString();
            }, year, month, date);
            datePickerDialog.show();
        });

        timeButton.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, minute1) -> {
                String timeString = hour1 + ":" + minute1;
                timePick.setText(timeString);
                prevTime = timePick.getText().toString();
            }, hour, minute, true);
            timePickerDialog.show();
        });
    }
}