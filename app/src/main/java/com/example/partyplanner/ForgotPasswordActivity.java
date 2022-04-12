package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth authentication;
    private TextInputLayout emailForgot;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgotpassword);

        TextView login = findViewById(R.id.forgotToLogin);
        Button reset = findViewById(R.id.reset);
        emailForgot = findViewById(R.id.emailForgot);
        progressBar = findViewById(R.id.progressbarForgot);
        authentication = FirebaseAuth.getInstance();

        reset.setOnClickListener(view -> {
            String email = emailForgot.getEditText().getText().toString().trim();

            if (email.isEmpty()) {
                emailForgot.setError("Email is required!");
                emailForgot.requestFocus();
                return;
            } else {
                emailForgot.setError(null);
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailForgot.setError("Please enter a valid email!");
                emailForgot.requestFocus();
                return;
            } else {
                emailForgot.setError(null);
            }

            progressBar.setVisibility(View.VISIBLE);
            authentication.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Check your mail to reset your password!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter the email you registered with!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            });
        });

        login.setOnClickListener(view -> {
            Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            finish();
            startActivity(i);
        });
    }
}
