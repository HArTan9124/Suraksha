package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class UserLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private EditText editTextSearch;
    private ImageButton imageButtonSearch;
    private Button btnDirection;

    private LatLng userLatLng, destinationLatLng;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        editTextSearch = findViewById(R.id.editTextSearch);
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        btnDirection = findViewById(R.id.btn_direction);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("MAP_ERROR", "Map Fragment is null!");
            Toast.makeText(this, "Error loading map", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.btn_refresh).setOnClickListener(view -> getUserLocation());
        imageButtonSearch.setOnClickListener(view -> searchLocation());

        // **Fixed Button Click Issue**
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShortestRoute();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("MAP_READY", "Google Map is ready!");

        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getUserLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            updateMapLocation(userLatLng);
                        } else {
                            Toast.makeText(this, "Unable to fetch location. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.e("LOCATION_ERROR", "Error getting location: " + e.getMessage()));
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void updateMapLocation(LatLng latLng) {
        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }
    }

    private void searchLocation() {
        String location = editTextSearch.getText().toString().trim();
        if (!location.isEmpty()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    destinationLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(location));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15f));
                } else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error in geocoding: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
    }

    private void getShortestRoute() {
        if (userLatLng == null || destinationLatLng == null) {
            Toast.makeText(this, "Set both current and destination locations!", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiKey = "YOUR_GOOGLE_MAPS_API_KEY"; // Replace with your API Key
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + userLatLng.latitude + "," + userLatLng.longitude +
                "&destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude +
                "&mode=driving" +
                "&key=" + apiKey;

        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray routes = jsonResponse.getJSONArray("routes");

                if (routes.length() > 0) {
                    JSONArray steps = routes.getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONArray("steps");

                    PolylineOptions polylineOptions = new PolylineOptions();
                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject step = steps.getJSONObject(i);
                        JSONObject start = step.getJSONObject("start_location");
                        JSONObject end = step.getJSONObject("end_location");

                        LatLng startLatLng = new LatLng(start.getDouble("lat"), start.getDouble("lng"));
                        LatLng endLatLng = new LatLng(end.getDouble("lat"), end.getDouble("lng"));

                        polylineOptions.add(startLatLng, endLatLng);
                    }

                    runOnUiThread(() -> {
                        mMap.addPolyline(polylineOptions);
                        Toast.makeText(this, "Route drawn!", Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error fetching route!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}