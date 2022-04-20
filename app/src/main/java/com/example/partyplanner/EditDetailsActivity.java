package com.example.partyplanner;

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

import java.util.List;
import java.util.Map;

public class EditDetailsActivity extends AppCompatActivity {
    ImageView editPhoto;
    TextInputLayout usernameUpdateHolder, phoneNumberUpdateHolder, addressLine1Holder, addressLine2Holder, addressLine3Holder;
    TextView changePhoto;
    Button cancel, update;
    ProgressBar progressbarUpdate;
    List<String> requestsSent;
    List<String> requestsReceived;
    Map<String, String> friendsList;

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

        usernameUpdateHolder = findViewById(R.id.usernameUpdateHolder);
        phoneNumberUpdateHolder = findViewById(R.id.phoneNumberUpdateHolder);
        addressLine1Holder = findViewById(R.id.addressLine1Holder);
        addressLine2Holder = findViewById(R.id.addressLine2Holder);
        addressLine3Holder = findViewById(R.id.addressLine3Holder);
        changePhoto = findViewById(R.id.changePhoto);
        editPhoto = findViewById(R.id.editPhoto);
        cancel = findViewById(R.id.cancelEdit);
        update = findViewById(R.id.updateEdit);
        progressbarUpdate = findViewById(R.id.progressbarUpdate);

        if (firebaseUser == null) {
            Toast.makeText(EditDetailsActivity.this, "Something went wrong! Your credentials are not available at the moment", Toast.LENGTH_LONG).show();
            progressbarUpdate.setVisibility(View.GONE);
        } else {
            Uri uri = firebaseUser.getPhotoUrl();
            Picasso.with(EditDetailsActivity.this).load(uri).into(editPhoto);
            String userId = firebaseUser.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User details = snapshot.getValue(User.class);
                    if (details != null) {
                        usernameUpdateHolder.getEditText().setText(details.username);
                        phoneNumberUpdateHolder.getEditText().setText(details.number);
                        addressLine1Holder.getEditText().setText(details.addressLine1);
                        addressLine2Holder.getEditText().setText(details.addressLine2);
                        addressLine3Holder.getEditText().setText(details.addressLine3);
                        requestsReceived = details.requestsReceived;
                        requestsSent = details.requestsSent;
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
            String name = usernameUpdateHolder.getEditText().getText().toString().trim();
            String phone = PhoneNumberUtils.formatNumber(phoneNumberUpdateHolder.getEditText().getText().toString().trim());
            String address1 = addressLine1Holder.getEditText().getText().toString().trim();
            String address2 = addressLine2Holder.getEditText().getText().toString().trim();
            String address3 = addressLine3Holder.getEditText().getText().toString().trim();

            if (name.isEmpty()) {
                usernameUpdateHolder.setError("Name is required!");
                usernameUpdateHolder.requestFocus();
                return;
            } else {
                usernameUpdateHolder.setError(null);
            }

            if (address1.isEmpty()) {
                addressLine1Holder.setError("Street and unit number is required!");
                addressLine1Holder.requestFocus();
                return;
            } else {
                addressLine1Holder.setError(null);
            }

            if (address2.isEmpty()) {
                addressLine2Holder.setError("City and State is required!");
                addressLine2Holder.requestFocus();
                return;
            } else {
                addressLine2Holder.setError(null);
            }

            if (address3.isEmpty()) {
                addressLine3Holder.setError("Pin is required!");
                addressLine3Holder.requestFocus();
                return;
            } else {
                addressLine3Holder.setError(null);
            }

            if (phone.isEmpty()) {
                phoneNumberUpdateHolder.setError("Phone number is required!");
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
            User user = new User(name, firebaseUser.getEmail(), phone, address1, address2, address3,
                    requestsReceived, requestsSent, friendsList);
            String userId = firebaseUser.getUid();
            if (uri != null) {
                StorageReference storageReference1 = storageReference.child(userId + "." + getFilesExtension(uri));
                storageReference1.putFile(uri).addOnSuccessListener(taskSnapshot -> storageReference1.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    firebaseUser = authentication.getCurrentUser();
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri1).build();
                    firebaseUser.updateProfile(request);
                }));
            }

            databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
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
