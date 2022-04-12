package com.example.partyplanner;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class ProfilePictureActivity extends AppCompatActivity {
    TouchImageView displayDP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profilepicture);

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();

        displayDP = findViewById(R.id.displayDP);

        if (firebaseUser == null) {
            Toast.makeText(ProfilePictureActivity.this, "Something went wrong! Your credentials are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            Uri uri = firebaseUser.getPhotoUrl();
            Picasso.with(ProfilePictureActivity.this).load(uri).into(displayDP);
        }
    }
}
