package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlacesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_places);

        Intent i = getIntent();
        String eventType = i.getStringExtra("events");
        String invitationCard = i.getStringExtra("backgroundImage");
        String eventName = i.getStringExtra("eventName");
        String date = i.getStringExtra("date");
        String time = i.getStringExtra("time");

        AutoCompleteTextView completeTextView = findViewById(R.id.autoComplete);
        completeTextView.setAdapter(new PlaceSuggestAdapter(PlacesActivity.this, android.R.layout.simple_list_item_1));

        completeTextView.setOnItemClickListener((adapterView, view, i1, l) -> {
            if (completeTextView.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            } else {
                String address = completeTextView.getText().toString().trim();
                Intent intent = new Intent(PlacesActivity.this, EventInvite.class);
                intent.putExtra("events", eventType);
                intent.putExtra("backgroundImage", invitationCard);
                intent.putExtra("eventName", eventName);
                intent.putExtra("address", address);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                finish();
                startActivity(intent);
            }
        });
    }
}
