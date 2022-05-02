package com.example.partyplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    private TextView accountName;
    private TextView emailAccount;
    private TextView phoneNumberAccount;
    private TextView addressLine1Account;
    private TextView addressLine2Account;
    private TextView addressLine3Account;
    private ProgressBar progressbar;
    private ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);
        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authentication.getCurrentUser();

        accountName = viewGroup.findViewById(R.id.accountName);
        emailAccount = viewGroup.findViewById(R.id.emailAccount);
        phoneNumberAccount = viewGroup.findViewById(R.id.phoneNumberAccount);
        addressLine1Account = viewGroup.findViewById(R.id.addressLine1Account);
        addressLine2Account = viewGroup.findViewById(R.id.addressLine2Account);
        addressLine3Account = viewGroup.findViewById(R.id.addressLine3Account);
        TextView preferencesEdit = viewGroup.findViewById(R.id.preferencesEdit);
        LinearLayout logout = viewGroup.findViewById(R.id.logout);
        Button editDetails = viewGroup.findViewById(R.id.editDetails);
        ImageView profilePicture = viewGroup.findViewById(R.id.profilePicture);
        progressbar = viewGroup.findViewById(R.id.progressbarLogout);

        progressbar.setVisibility(View.VISIBLE);
        if (firebaseUser == null) {
            Toast.makeText(AccountFragment.this.getActivity(), "Something went wrong! Your credentials are not available at the moment", Toast.LENGTH_LONG).show();
            progressbar.setVisibility(View.GONE);
        } else {
            String userId = firebaseUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User details = snapshot.getValue(User.class);
                    if (details != null) {
                        Picasso.with(AccountFragment.this.getActivity()).load(Uri.parse(details.profileImage)).into(profilePicture);
                        accountName.setText(details.username);
                        emailAccount.setText(firebaseUser.getEmail());
                        phoneNumberAccount.setText(details.number);
                        addressLine1Account.setText(details.address.get(0));
                        addressLine2Account.setText(details.address.get(1));
                        addressLine3Account.setText(details.address.get(2));
                    } else {
                        Toast.makeText(AccountFragment.this.getActivity(), "User not found!!", Toast.LENGTH_LONG).show();
                    }
                    progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AccountFragment.this.getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            });
        }

        profilePicture.setOnClickListener(view -> startActivity(new Intent(AccountFragment.this.getActivity(), ProfilePictureActivity.class)));

        editDetails.setOnClickListener(view -> startActivity(new Intent(AccountFragment.this.getActivity(), EditDetailsActivity.class)));

        preferencesEdit.setOnClickListener(view -> startActivity(new Intent(AccountFragment.this.getActivity(), PreferencesActivity.class)));

        logout.setOnClickListener(view -> {
            progressbar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(AccountFragment.this.getActivity(), LoginActivity.class);
            getActivity().finish();
            AccountFragment.this.getActivity().startActivity(i);
            progressbar.setVisibility(View.GONE);
            Toast.makeText(AccountFragment.this.getActivity(), "Successfully logged out!", Toast.LENGTH_SHORT).show();
        });
    }
}
