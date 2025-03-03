package com.example.womensafety;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ImageButton linkedin1 = findViewById(R.id.linkedin);
        ImageButton instagram1 = findViewById(R.id.instagram);
        ImageButton git1 = findViewById(R.id.git);
        linkedin1.setOnClickListener(view -> openUrl("https://www.linkedin.com/in/hartan9124/"));
        instagram1.setOnClickListener(view -> openUrl("https://www.instagram.com/tandon._.2895/"));
        git1.setOnClickListener(view -> openUrl("https://github.com/HArTan9124"));

        ImageButton linkedin4 = findViewById(R.id.linkedin4);
        ImageButton instagram4 = findViewById(R.id.instagram4);
        ImageButton git4 = findViewById(R.id.git4);
        linkedin4.setOnClickListener(view -> openUrl("https://www.linkedin.com/in/sparsh08?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"));
        instagram4.setOnClickListener(view -> openUrl("https://www.instagram.com/sparsh0806/"));
        git4.setOnClickListener(view -> openUrl("https://github.com/sparsh-sharma-08"));

        ImageButton linkedin5 = findViewById(R.id.linkedin5);
        ImageButton instagram5 = findViewById(R.id.instagram5);
        ImageButton git5 = findViewById(R.id.git5);
        linkedin5.setOnClickListener(view -> openUrl("https://www.linkedin.com/in/praval-pratap-singh-chauhan-91222028a/"));
        instagram5.setOnClickListener(view -> openUrl("https://www.instagram.com/pravalchauhan18?igsh=Zmg3bmJiMWhkZzZt"));
        git5.setOnClickListener(view -> openUrl("https://github.com/Praval-Chauhan"));
    }
    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
