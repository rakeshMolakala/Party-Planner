package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText username, emailSignup, passwordSignup, rePasswordSignup;
    TextView Stol;
    Button signup;
    ProgressBar progressbar;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);

        authentication = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        emailSignup = findViewById(R.id.emailSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        rePasswordSignup = findViewById(R.id.rePasswordSignup);
        Stol = findViewById(R.id.SignupToLogin);
        signup = findViewById(R.id.signup);
        progressbar = findViewById(R.id.progressbarSignup);

        signup.setOnClickListener(view -> {
            rePasswordSignup.onEditorAction(EditorInfo.IME_ACTION_DONE);

            String name = username.getText().toString().trim();
            String email = emailSignup.getText().toString().trim();
            String password = passwordSignup.getText().toString().trim();
            String conPassword = rePasswordSignup.getText().toString().trim();

            if (name.isEmpty()) {
                username.setError("Username is required!");
                username.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                emailSignup.setError("Email is required!");
                emailSignup.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailSignup.setError("Please enter a valid email!");
                emailSignup.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordSignup.setError("Password is required!");
                passwordSignup.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordSignup.setError("Password should consist a minimum of 6 characters!");
                passwordSignup.requestFocus();
                return;
            }

            if (conPassword.isEmpty()) {
                rePasswordSignup.setError("Confirm Password!");
                rePasswordSignup.requestFocus();
                return;
            }

            if (!password.equals(conPassword)) {
                rePasswordSignup.setError("Passwords do not match!");
                rePasswordSignup.requestFocus();
                return;
            }

            progressbar.setVisibility(View.VISIBLE);

            authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = new User(name, email);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Successfully registered as " + name, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
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
            startActivity(i);
        });
    }
}
