package com.example.partyplanner;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    private static MainActivity instance;
    Handler handler;

    public static MainActivity getInstance() {
        return instance;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        instance = this;

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.chat:
                    fragment = new ChatFragment();
                    break;
                case R.id.events:
                    fragment = new EventsFragment();
                    break;
                case R.id.ToDo:
                    fragment = new ToDoFragment();
                    break;
                case R.id.Account:
                    fragment = new AccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments, fragment).commit();
            return true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragments, new HomeFragment()).commit();
        RunEveryFriday();
    }

    private void RunEveryFriday() {
        handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String day = LocalDate.now().getDayOfWeek().name();
                if (day.equals("FRIDAY")) {
                    sendNotification("No User", "Yayyy, its FRIYAYY");
                }
                handler.postDelayed(this, 3600000);
            }
        };
        handler.post(run);
    }

    public void sendNotification(String sentUser, String message) {
//        int sentImageInt = Integer.parseInt(sentImage);
//        int sendingImage = 0;
        Log.d("Notification1992", "Start of notification");
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.user);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,
                default_notification_channel_id)
                .setSmallIcon(R.drawable.user)
                .setLargeIcon(icon)
                .setAutoCancel(true)
                .setPriority(1)
                .setContentTitle("Received New Notification")
                .setContentText(message);
        NotificationManager mngr = (NotificationManager) getSystemService(MainActivity.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notification channel", importance);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel.setDescription("Just a notification description");
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        mngr.createNotificationChannel(channel);
        mngr.notify((int) System.currentTimeMillis(), builder.build());
        Log.d("Notif1993", "End of notification");
    }
}
