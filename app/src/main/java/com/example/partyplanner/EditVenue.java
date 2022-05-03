package com.example.partyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

public class EditVenue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_venue);
        getSupportActionBar().hide();
        Intent i = getIntent();
        String currEvent = i.getStringExtra("currEvent");
        String prevTime = i.getStringExtra("prevTime");
        String prevDate = i.getStringExtra("prevDate");
        String eventName = i.getStringExtra("eventName");


        AutoCompleteTextView completeTextView = findViewById(R.id.autoComplete);
        completeTextView.setAdapter(new PlaceSuggestAdapter(EditVenue.this, android.R.layout.simple_list_item_1));

        completeTextView.setOnItemClickListener((adapterView, view, i1, l) -> {
            String address = completeTextView.getText().toString();
            Intent intent = new Intent(EditVenue.this, EditActivity.class);
            intent.putExtra("currEvent", currEvent);
            intent.putExtra("type","venue");
            intent.putExtra("prevDate",prevDate);
            intent.putExtra("prevTime",prevTime);
            intent.putExtra("eventName",eventName);
            intent.putExtra("address", address);
            finish();
            startActivity(intent);
        });
    }
}
