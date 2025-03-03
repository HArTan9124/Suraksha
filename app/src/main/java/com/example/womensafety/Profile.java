package com.example.womensafety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private TextView textViewName, textViewGender, textViewContactNumber;
    private TextView textViewDOB, textViewDescribe;
    private ImageView profileImage;
    private ImageButton imageButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            fetchUserData();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        textViewName = findViewById(R.id.textViewName);
        textViewGender = findViewById(R.id.textViewGender);
        textViewContactNumber = findViewById(R.id.textViewContactNumber);
        textViewDOB = findViewById(R.id.textViewDOB);
        textViewDescribe = findViewById(R.id.textViewDescribe);
        profileImage = findViewById(R.id.circularprofile);
        imageButton = findViewById(R.id.logoutbtn);

        imageButton.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        Button yesButton = dialog.findViewById(R.id.yesbtn);
        Button noButton = dialog.findViewById(R.id.nobtn);

        yesButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Profile.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        noButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void fetchUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserData userData = snapshot.getValue(UserData.class);

                    if (userData != null) {
                        textViewName.setText("Name: " + (userData.getName() != null ? userData.getName() : "N/A"));
                        textViewGender.setText("Gender: " + (userData.gender != null ? userData.gender : "N/A"));
                        textViewContactNumber.setText("Your Number: " + (userData.contactNumber != null ? userData.contactNumber : "N/A"));
                        textViewDOB.setText("Date of Birth: " + (userData.dob != null ? userData.dob : "N/A"));
                        textViewDescribe.setText("Description: " + (userData.description != null ? userData.description : "N/A"));

                        // Handle Profile Picture
                        if (userData.profilePicture != null) {
                            switch (userData.profilePicture) {
                                case "dpb":
                                    profileImage.setImageResource(R.drawable.dpb);
                                    break;
                                case "dpg":
                                    profileImage.setImageResource(R.drawable.dpg);
                                    break;
                                default:
                                    profileImage.setImageResource(R.drawable.viewprofiler);
                                    break;
                            }
                        } else {
                            profileImage.setImageResource(R.drawable.viewprofiler);
                        }

                    } else {
                        Toast.makeText(Profile.this, "User data is null!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Profile.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Profile", "Error fetching data: " + error.getMessage());
                Toast.makeText(Profile.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
