package com.example.womensafety;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Emergency extends AppCompatActivity {

    private Button btnPolice, btnWomenHelpline, btnFireBrigade, btnAmbulance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency); // Set the layout


        btnPolice = findViewById(R.id.btnPolice);
        btnWomenHelpline = findViewById(R.id.btnWomenHelpline);
        btnFireBrigade = findViewById(R.id.btnFireBrigade);
        btnAmbulance = findViewById(R.id.btnAmbulance);


        btnPolice.setOnClickListener(v -> callEmergencyService("100"));
        btnWomenHelpline.setOnClickListener(v -> callEmergencyService("1091"));
        btnFireBrigade.setOnClickListener(v -> callEmergencyService("101"));
        btnAmbulance.setOnClickListener(v -> callEmergencyService("102"));
    }

    private void callEmergencyService(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);  // Start the call
    }
}
