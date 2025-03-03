package com.example.womensafety;

import android.os.Bundle;
import android.text.TextUtils;
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

public class Chat extends AppCompatActivity {

    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView chatRecyclerView;
    private List<Messageeee> messageList;
    private ChatAdaptor chatAdapter;
    private DatabaseReference chatRef;
    private FirebaseAuth mAuth;
    private String senderId, receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize views
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdaptor(messageList, this);
        chatRecyclerView.setAdapter(chatAdapter);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is logged in
        if (mAuth.getCurrentUser() != null) {
            senderId = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get receiverId from intent
        receiverId = getIntent().getStringExtra("userId");
        if (receiverId == null) {
            Toast.makeText(this, "Receiver ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        chatRef = FirebaseDatabase.getInstance().getReference("Chats");

        sendButton.setOnClickListener(v -> sendMessage());
        loadMessages();
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String messageId = chatRef.push().getKey();
        if (messageId == null) {
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
            return;
        }

        Messageeee message = new Messageeee(senderId, messageText, System.currentTimeMillis());

        // Save message to both sender's and receiver's chat history
        chatRef.child(senderId).child(receiverId).child(messageId).setValue(message);
        chatRef.child(receiverId).child(senderId).child(messageId).setValue(message);

        messageInput.setText(""); // Clear input field
    }

    private void loadMessages() {
        chatRef.child(senderId).child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messageeee message = dataSnapshot.getValue(Messageeee.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
