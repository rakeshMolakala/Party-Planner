package com.example.partyplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();

        ImageView homeProfile = viewGroup.findViewById(R.id.homeProfile);
        CardView birthdays = viewGroup.findViewById(R.id.birthdays);
        CardView wedding = viewGroup.findViewById(R.id.wedding);
        CardView pizza = viewGroup.findViewById(R.id.pizza);
        CardView christmas = viewGroup.findViewById(R.id.christmas);
        CardView halloween = viewGroup.findViewById(R.id.halloween);
        CardView dance = viewGroup.findViewById(R.id.dance);

        if (firebaseUser == null) {
            Toast.makeText(HomeFragment.this.getActivity(), "Something went wrong! Your credentials are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            Uri uri = firebaseUser.getPhotoUrl();
            if (firebaseUser.getPhotoUrl() == null) {
                homeProfile.setImageResource(R.drawable.user);
            } else {
                Picasso.with(HomeFragment.this.getActivity()).load(uri).into(homeProfile);
            }
        }

        homeProfile.setOnClickListener(view -> startActivity(new Intent(HomeFragment.this.getActivity(), ProfilePictureActivity.class)));

        birthdays.setOnClickListener(view -> {
            Intent i = new Intent(HomeFragment.this.getActivity(), EventActivity.class);
            i.putExtra("event", "birthday");
            startActivity(i);
        });

        wedding.setOnClickListener(view -> {
            Intent i = new Intent(HomeFragment.this.getActivity(), EventActivity.class);
            i.putExtra("event", "wedding");
            startActivity(i);
        });

        pizza.setOnClickListener(view -> {
            Intent i = new Intent(HomeFragment.this.getActivity(), EventActivity.class);
            i.putExtra("event", "pizza");
            startActivity(i);
        });

        christmas.setOnClickListener(view -> {
            Intent i = new Intent(HomeFragment.this.getActivity(), EventActivity.class);
            i.putExtra("event", "christmas");
            startActivity(i);
        });

        halloween.setOnClickListener(view -> {
            Intent i = new Intent(HomeFragment.this.getActivity(), EventActivity.class);
            i.putExtra("event", "halloween");
            startActivity(i);
        });

        dance.setOnClickListener(view -> {
            Intent i = new Intent(HomeFragment.this.getActivity(), EventActivity.class);
            i.putExtra("event", "dance");
            startActivity(i);
        });
        return viewGroup;
    }
}