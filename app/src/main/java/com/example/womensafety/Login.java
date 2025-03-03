package com.example.womensafety;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            redirectToMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.emailLi);
        EditText passwordEditText = findViewById(R.id.passwordLI);
        Button loginButton = findViewById(R.id.Loginbtn);
        TextView createAccountTextView = findViewById(R.id.Logintxt);
        TextView forgotPasswordTextView = findViewById(R.id.forgotpass);
        TextView guestLoginTextView = findViewById(R.id.guestLogin);

        if (savedInstanceState != null) {
            emailEditText.setText(savedInstanceState.getString("email", ""));
            passwordEditText.setText(savedInstanceState.getString("password", ""));
        }

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                loginWithFirebase(email, password);
            }
        });

        guestLoginTextView.setOnClickListener(view -> {
            redirectToMainActivity();
        });

        createAccountTextView.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, CreateAcc.class);
            startActivity(intent);
        });

        forgotPasswordTextView.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                sendPasswordResetEmail(email);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText emailEditText = findViewById(R.id.emailLi);
        EditText passwordEditText = findViewById(R.id.passwordLI);

        outState.putString("email", emailEditText.getText().toString());
        outState.putString("password", passwordEditText.getText().toString());
    }

    private void loginWithFirebase(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        redirectToMainActivity();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(this, "Failed to login: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendPasswordResetEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(Login.this, "Failed to send reset email: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
