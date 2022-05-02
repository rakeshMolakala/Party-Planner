package com.example.partyplanner;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EditDetailsActivity extends AppCompatActivity {
    ImageView editPhoto;
    TextInputLayout phoneNumberUpdateHolder, addressLine1Holder, addressLine2Holder, addressLine3Holder;
    TextView changePhoto;
    Button cancel, update;
    ProgressBar progressbarUpdate;
    List<String> requestsSent, requestsReceived, address;
    List<List<String>> preferences;
    Map<String, List<String>> friendsList;
    String Username;

    private DatabaseReference databaseReference;
    private FirebaseAuth authentication;
    private FirebaseUser firebaseUser;
    private Uri uri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_editdetails);

        authentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseUser = authentication.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures");

        phoneNumberUpdateHolder = findViewById(R.id.phoneNumberUpdateHolder);
        addressLine1Holder = findViewById(R.id.addressLine1Holder);
        addressLine2Holder = findViewById(R.id.addressLine2Holder);
        addressLine3Holder = findViewById(R.id.addressLine3Holder);
        changePhoto = findViewById(R.id.changePhoto);
        editPhoto = findViewById(R.id.editPhoto);
        cancel = findViewById(R.id.cancelEdit);
        update = findViewById(R.id.updateEdit);
        address = new ArrayList<>();
        progressbarUpdate = findViewById(R.id.progressbarUpdate);

        if (firebaseUser == null) {
            Toast.makeText(EditDetailsActivity.this, "Something went wrong! Your credentials are not available at the moment", Toast.LENGTH_LONG).show();
            progressbarUpdate.setVisibility(View.GONE);
        } else {
            Uri uri = firebaseUser.getPhotoUrl();
            String userId = firebaseUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User details = snapshot.getValue(User.class);
                    if (details != null) {
                        if (details.profileImage.length() == 5) {
                            editPhoto.setImageResource(R.drawable.user);
                        } else {
                            Picasso.with(EditDetailsActivity.this).load(uri).into(editPhoto);
                        }
                        Username = details.username;
                        phoneNumberUpdateHolder.getEditText().setText(details.number);
                        addressLine1Holder.getEditText().setText(details.address.get(0));
                        addressLine2Holder.getEditText().setText(details.address.get(1));
                        addressLine3Holder.getEditText().setText(details.address.get(2));
                        requestsReceived = details.requestsReceived;
                        requestsSent = details.requestsSent;
                        preferences = details.preferences;
                        friendsList = details.friendsList;
                    } else {
                        Toast.makeText(EditDetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    progressbarUpdate.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditDetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressbarUpdate.setVisibility(View.GONE);
                }
            });
        }


        changePhoto.setOnClickListener(view -> ImagePicker.Companion.with(EditDetailsActivity.this).crop().cropOval().start());

        editPhoto.setOnClickListener(view -> ImagePicker.Companion.with(EditDetailsActivity.this).crop().cropOval().start());

        cancel.setOnClickListener(view -> {
            Intent i = new Intent(EditDetailsActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        });

        update.setOnClickListener(view -> {
            String phone = PhoneNumberUtils.formatNumber(phoneNumberUpdateHolder.getEditText().getText().toString().trim());
            String address1 = addressLine1Holder.getEditText().getText().toString().trim();
            String address2 = addressLine2Holder.getEditText().getText().toString().trim();
            String address3 = addressLine3Holder.getEditText().getText().toString().trim();

            if (phone.isEmpty()) {
                phoneNumberUpdateHolder.setError("Phone number is required!");
                phoneNumberUpdateHolder.requestFocus();
                return;
            } else {
                phoneNumberUpdateHolder.setError(null);
            }

            if (phone.length() < 10) {
                phoneNumberUpdateHolder.setError("Phone number must consist of atleast 10 digits!");
                phoneNumberUpdateHolder.requestFocus();
                return;
            } else {
                phoneNumberUpdateHolder.setError(null);
            }

            if (!Patterns.PHONE.matcher(phone).matches()) {
                phoneNumberUpdateHolder.setError("Check format of the given phone number!");
                phoneNumberUpdateHolder.requestFocus();
                return;
            } else {
                phoneNumberUpdateHolder.setError(null);
            }

            progressbarUpdate.setVisibility(View.VISIBLE);
            address.add(0, address1);
            address.add(1, address2);
            address.add(2, address3);
            String currUserPhotoUrl;
            if (firebaseUser.getPhotoUrl() != null) {
                currUserPhotoUrl = firebaseUser.getPhotoUrl().toString();
            }
            else {
                currUserPhotoUrl = "\"jkh\"";
            }
            User user = new User(Username, firebaseUser.getEmail(), phone, address,
                    requestsReceived, requestsSent, preferences, friendsList, currUserPhotoUrl);
            String userId = firebaseUser.getUid();
            if (uri != null) {
                StorageReference storageReference1 = storageReference.child(userId + "." + getFilesExtension(uri));
                storageReference1.putFile(uri).addOnSuccessListener(taskSnapshot -> storageReference1.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    firebaseUser = authentication.getCurrentUser();
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri1).build();
                    firebaseUser.updateProfile(request);
                    databaseReference.child(userId).child("profileImage").setValue(uri1.toString());
                }));
            }

            databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(Username).build();
                    firebaseUser.updateProfile(request);
                    Toast.makeText(EditDetailsActivity.this, "Update Successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditDetailsActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(EditDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressbarUpdate.setVisibility(View.GONE);
            });
        });
    }

    private String getFilesExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
        editPhoto.setImageURI(uri);
    }
}