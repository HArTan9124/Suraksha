package com.example.womensafety;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Addfrnd extends AppCompatActivity implements UserAdaptor.OnUserClickListener {
    FirebaseAuth auth;
    RecyclerView recyclerView;
    UserAdaptor adaptor;
    FirebaseDatabase database;
    ImageButton imageButton;
    EditText searchInput;
    List<UserData> userList;
    List<UserData> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfrnd);

        imageButton = findViewById(R.id.profileviewer);
        searchInput = findViewById(R.id.search_text);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        userList = new ArrayList<>();
        filteredList = new ArrayList<>();

        recyclerView = findViewById(R.id.mainuser_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptor = new UserAdaptor(filteredList, this, this);
        recyclerView.setAdapter(adaptor);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    fetchUsers(s.toString().trim());
                } else {
                    filteredList.clear();
                    adaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent(Addfrnd.this, Profile.class);
            startActivity(intent);
        });

        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(Addfrnd.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    private void fetchUsers(String query) {
        DatabaseReference reference = database.getReference("Users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                filteredList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserData userData = snapshot.getValue(UserData.class);
                    if (userData != null && userData.name.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(userData);
                    }
                }
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onUserClick(UserData user) {
        Intent intent = new Intent(Addfrnd.this, Chat.class);
//        intent.putExtra("userId", user.getUid());
        intent.putExtra("userName", user.getName());
        startActivity(intent);
    }
}
