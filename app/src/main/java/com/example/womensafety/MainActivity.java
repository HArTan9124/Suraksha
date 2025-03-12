package com.example.womensafety;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LottieAnimationView ai, lottieAnimationView;
    private ViewPager2 viewPager;
    private final Handler handler = new Handler();
    private int currentPage = 0;
    private Runnable autoScrollRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        drawerLayout = findViewById(R.id.main);
        ai = findViewById(R.id.ai);
        navigationView = findViewById(R.id.navview);
        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        // Setup Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // AI Bot Click Listener
        ai.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Surakshabott.class)));

        // Initialize Location Services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Setup SeekBar for Location Sharing
        SeekBar seekBar = findViewById(R.id.sliderSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 50) {
                    shareLiveLocation();
                    seekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Setup Lottie Animation
        lottieAnimationView = findViewById(R.id.lottieView);
        lottieAnimationView.setAnimation(R.raw.womenoo);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);

        // Setup ViewPager
        viewPager = findViewById(R.id.viewPager);
        List<CardModel> cards = new ArrayList<>();
        cards.add(new CardModel(R.drawable.trialll));
        cards.add(new CardModel(R.drawable.screenshot_2024_12_19_225618));
        cards.add(new CardModel(R.drawable.kuchbbb));
        cards.add(new CardModel(R.drawable.screenshot_2024_12_19_224955));

        viewPager.setAdapter(new CardAdapter(cards));

        // Auto Scroll ViewPager
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == cards.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(autoScrollRunnable, 2000);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.health) {
            startActivity(new Intent(this, health.class));
        } else if (id == R.id.emergency) {
            startActivity(new Intent(this, Emergency.class));
        }
//        } else if (id == R.id.addmore) {
//            startActivity(new Intent(this, Addfrnd.class));
//        }
          else if (id == R.id.Profile) {
            startActivity(new Intent(this, Profile.class));
        } else if (id == R.id.aboutus) {
            startActivity(new Intent(this, AboutUs.class));
        } else if (id == R.id.Location) {
            startActivity(new Intent(this, UserLocation.class));
        }
//        else if (id == R.id.PeriodCalculator) {
////            startActivity(new Intent(this, Calender.class));
//        }
        else if (id == R.id.logout) {
            showLogoutDialog();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawers();
        return true;
    }

    private void showLogoutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);

        Button yesButton = dialog.findViewById(R.id.yesbtn);
        Button noButton = dialog.findViewById(R.id.nobtn);

        yesButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        noButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void shareLiveLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                String locationMessage = "I am in danger. Please help! My location: " +
                        "https://www.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, locationMessage);
                sendIntent.setPackage("com.whatsapp");

                try {
                    startActivity(sendIntent);
                    Toast.makeText(this, "Location shared successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Unable to fetch location. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoScrollRunnable != null) {
            handler.removeCallbacks(autoScrollRunnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
