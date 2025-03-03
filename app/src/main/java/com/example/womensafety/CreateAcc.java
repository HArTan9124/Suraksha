package com.example.womensafety;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class CreateAcc extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        firebaseAuth = FirebaseAuth.getInstance();
        EditText emailEditText = findViewById(R.id.emailca);
        EditText passwordEditText = findViewById(R.id.passwordca);
        Button createAccountButton = findViewById(R.id.createaccbtn);
        TextView loginTextView = findViewById(R.id.createacctxt);
        createAccountButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {

                createFirebaseAccount(email, password);
            }
        });

        loginTextView.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAcc.this, Login.class);
            startActivity(intent);
        });
    }
    private void createFirebaseAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateAcc.this, YourData.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to create account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}