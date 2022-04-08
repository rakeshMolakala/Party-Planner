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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailLogin, passwordLogin;
    private TextView ltoS, forgotPassword;
    private Button login;
    private ProgressBar progressbar;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        authentication = FirebaseAuth.getInstance();
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        login = findViewById(R.id.login);
        ltoS = findViewById(R.id.loginToSignup);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressbar = findViewById(R.id.progressbarLogin);

        login.setOnClickListener(view -> {
            passwordLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);
            String email = emailLogin.getText().toString().trim();
            String password = passwordLogin.getText().toString().trim();

            if (email.isEmpty()) {
                emailLogin.setError("Email is required!");
                emailLogin.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLogin.setError("Please enter a valid email!");
                emailLogin.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordLogin.setError("Password is required!");
                passwordLogin.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordLogin.setError("Password should consist a minimum of 6 characters!");
                passwordLogin.requestFocus();
                return;
            }

            progressbar.setVisibility(View.VISIBLE);

            authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Please check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! Check credentials.", Toast.LENGTH_LONG).show();
                }
                progressbar.setVisibility(View.GONE);
            });
        });

        forgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        ltoS.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
        });
    }
}