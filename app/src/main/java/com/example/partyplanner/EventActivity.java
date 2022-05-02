package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event);

        Intent i = getIntent();
        String selectedEvent = i.getStringExtra("event");

        CardView cardView1 = findViewById(R.id.eventCardView1);
        CardView cardView2 = findViewById(R.id.eventCardView2);

        ImageView card1 = findViewById(R.id.card1);
        ImageView card2 = findViewById(R.id.card2);

        Intent sendingIntent = new Intent(EventActivity.this, EventInvite.class);

        switch (selectedEvent) {
            case "birthday":
                card1.setImageResource(R.drawable.birthday1);
                card2.setImageResource(R.drawable.birthday2);
                sendingIntent.putExtra("event", "birthday");
                break;
            case "wedding":
                card1.setImageResource(R.drawable.wedding1);
                card2.setImageResource(R.drawable.wedding2);
                sendingIntent.putExtra("event", "wedding");

                break;
            case "pizza":
                card1.setImageResource(R.drawable.pizza1);
                card2.setImageResource(R.drawable.pizza2);
                sendingIntent.putExtra("event", "pizza");

                break;
            case "christmas":
                card1.setImageResource(R.drawable.christmas1);
                card2.setImageResource(R.drawable.christmas2);
                sendingIntent.putExtra("event", "christmas");

                break;
            case "halloween":
                card1.setImageResource(R.drawable.halloween1);
                card2.setImageResource(R.drawable.halloween2);
                sendingIntent.putExtra("event", "halloween");

                break;
            case "dance":
                card1.setImageResource(R.drawable.dance1);
                card2.setImageResource(R.drawable.dance2);
                sendingIntent.putExtra("event", "dance");
                break;
        }

        cardView1.setOnClickListener(view -> {
            sendingIntent.putExtra("backgroundImage", "1");
            startActivity(sendingIntent);
        });

        cardView2.setOnClickListener(view -> {
            sendingIntent.putExtra("backgroundImage", "2");
            startActivity(sendingIntent);
        });
    }
}