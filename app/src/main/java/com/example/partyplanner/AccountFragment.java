package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {
    private Button logout;
    private ProgressBar progressbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);
        logout = viewGroup.findViewById(R.id.logout);
        progressbar = viewGroup.findViewById(R.id.progressbarLogout);
        logout.setOnClickListener(view -> {
            progressbar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(AccountFragment.this.getActivity(), LoginActivity.class);
            getActivity().finish();
            AccountFragment.this.getActivity().startActivity(i);
            progressbar.setVisibility(View.GONE);
            Toast.makeText(AccountFragment.this.getActivity(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
        });
        return viewGroup;
    }
}
