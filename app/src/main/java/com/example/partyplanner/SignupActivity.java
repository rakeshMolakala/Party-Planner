package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout usernameHolder, emailSignupHolder, passwordSignupHolder, rePasswordSignupHolder;
    private TextView Stol;
    private Button signup;
    private ProgressBar progressbar;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        authentication = FirebaseAuth.getInstance();
        usernameHolder = findViewById(R.id.usernameHolder);
        emailSignupHolder = findViewById(R.id.emailSignupHolder);
        passwordSignupHolder = findViewById(R.id.passwordSignupHolder);
        rePasswordSignupHolder = findViewById(R.id.rePasswordSignupHolder);
        Stol = findViewById(R.id.SignupToLogin);
        signup = findViewById(R.id.signup);
        progressbar = findViewById(R.id.progressbarSignup);

        signup.setOnClickListener(view -> {
            rePasswordSignupHolder.getEditText().onEditorAction(EditorInfo.IME_ACTION_DONE);

            String name = usernameHolder.getEditText().getText().toString().trim();
            String email = emailSignupHolder.getEditText().getText().toString().trim();
            String password = passwordSignupHolder.getEditText().getText().toString().trim();
            String conPassword = rePasswordSignupHolder.getEditText().getText().toString().trim();

            if (name.isEmpty()) {
                usernameHolder.setError("Username is required!");
                usernameHolder.requestFocus();
                return;
            } else {
                usernameHolder.setError(null);
            }

            if (email.isEmpty()) {
                emailSignupHolder.setError("Email is required!");
                emailSignupHolder.requestFocus();
                return;
            } else {
                emailSignupHolder.setError(null);
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailSignupHolder.setError("Please enter a valid email!");
                emailSignupHolder.requestFocus();
                return;
            } else {
                emailSignupHolder.setError(null);
            }

            if (password.isEmpty()) {
                passwordSignupHolder.setError("Password is required!");
                passwordSignupHolder.requestFocus();
                return;
            } else {
                passwordSignupHolder.setError(null);
            }

            if (password.length() < 6) {
                passwordSignupHolder.setError("Password should consist a minimum of 6 characters!");
                passwordSignupHolder.requestFocus();
                return;
            } else {
                passwordSignupHolder.setError(null);
            }

            if (conPassword.isEmpty()) {
                rePasswordSignupHolder.setError("Confirm Password!");
                rePasswordSignupHolder.requestFocus();
                return;
            } else {
                rePasswordSignupHolder.setError(null);
            }

            if (!password.equals(conPassword)) {
                rePasswordSignupHolder.setError("Passwords do not match!");
                rePasswordSignupHolder.requestFocus();
                return;
            } else {
                rePasswordSignupHolder.setError(null);
            }

            progressbar.setVisibility(View.VISIBLE);

            authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = new User(name, email);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Successfully registered as " + name, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                            finish();
                            startActivity(i);
                        } else {
                            Toast.makeText(SignupActivity.this, "Failed to register! Try again.", Toast.LENGTH_LONG).show();
                        }
                        progressbar.setVisibility(View.GONE);
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Failed to register! Try again.", Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            });
        });

        Stol.setOnClickListener(view -> {
            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
            finish();
            startActivity(i);
        });
    }
}
