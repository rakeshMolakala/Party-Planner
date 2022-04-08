package com.example.partyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth authentication;
    private TextView login;
    private Button reset;
    private EditText emailForgot;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgotpassword);

        login = findViewById(R.id.forgotToLogin);
        reset = findViewById(R.id.reset);
        emailForgot = findViewById(R.id.emailForgot);
        progressBar = findViewById(R.id.progressbarForgot);
        authentication = FirebaseAuth.getInstance();

        reset.setOnClickListener(view -> {
            String email = emailForgot.getText().toString().trim();

            if (email.isEmpty()) {
                emailForgot.setError("Email is required!");
                emailForgot.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailForgot.setError("Please enter a valid email!");
                emailForgot.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            authentication.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Check your mail to reset your password!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Something is wrong! Try again!", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        login.setOnClickListener(view -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        });
    }
}
