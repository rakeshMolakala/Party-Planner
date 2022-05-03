package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

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

        AutoCompleteTextView completeTextView = findViewById(R.id.autoComplete);
        completeTextView.setAdapter(new PlaceSuggestAdapter(PlacesActivity.this, android.R.layout.simple_list_item_1));

        completeTextView.setOnItemClickListener((adapterView, view, i1, l) -> {
            String address = completeTextView.getText().toString();
            Intent intent = new Intent(PlacesActivity.this, EventInvite.class);
            intent.putExtra("events", eventType);
            intent.putExtra("backgroundImage", invitationCard);
            intent.putExtra("address", address);
            finish();
            startActivity(intent);
        });
    }
}
