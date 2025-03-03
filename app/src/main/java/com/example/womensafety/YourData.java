package com.example.womensafety;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class YourData extends AppCompatActivity {

    ImageView img1, img2;
    Button buttonSave;
    Spinner spinnerGender;
    EditText editTextName, editTextContactNumber, editTextDOB, editTextdescribe;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    String selectedImage = null;
    String selectedGender = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_data);

        // Initialize views
        img1 = findViewById(R.id.dpb);
        img2 = findViewById(R.id.dpg);
        buttonSave = findViewById(R.id.buttonSave);
        spinnerGender = findViewById(R.id.spinnerGender);
        editTextName = findViewById(R.id.editTextName);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextDOB = findViewById(R.id.editTextdob);
        editTextdescribe = findViewById(R.id.editTextdescribe);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Set up gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGender = null;
            }
        });

        // Date picker dialog for DOB
        editTextDOB.setOnClickListener(v -> showDatePicker());

        // Image Click Listeners
        img1.setOnClickListener(v -> selectImage("dpb"));
        img2.setOnClickListener(v -> selectImage("dpg"));

        // Save Button Click Listener
        buttonSave.setOnClickListener(v -> saveAllData());
    }

    private void showDatePicker() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Open date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editTextDOB.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void selectImage(String image) {
        selectedImage = image;
        if (image.equals("dpb")) {
            img1.setAlpha(1.0f);
            img2.setAlpha(0.5f);
        } else {
            img1.setAlpha(0.5f);
            img2.setAlpha(1.0f);
        }
    }

    private void saveAllData() {
        String name = editTextName.getText().toString().trim();
        String contactNumber = editTextContactNumber.getText().toString().trim();
        String dob = editTextDOB.getText().toString().trim();
        String description = editTextdescribe.getText().toString().trim();

        if (selectedImage == null) {
            Toast.makeText(this, "Please select a profile picture!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedGender == null || selectedGender.equals("Select Gender")) {
            Toast.makeText(this, "Please select your gender!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contactNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your contact number!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dob.isEmpty()) {
            Toast.makeText(this, "Please enter your date of birth!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.isEmpty()) {
            Toast.makeText(this, "Please describe yourself!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child(userId).child("profilePicture").setValue(selectedImage);
        databaseReference.child(userId).child("gender").setValue(selectedGender);
        databaseReference.child(userId).child("name").setValue(name);
        databaseReference.child(userId).child("contactNumber").setValue(contactNumber);
        databaseReference.child(userId).child("dob").setValue(dob);
        databaseReference.child(userId).child("description").setValue(description)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(YourData.this, "Data Saved Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(YourData.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(YourData.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
