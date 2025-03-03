package com.example.womensafety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Surakshabott extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private List<Message> messageList;
    private MessAdaptor messageAdapter;

    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json");
    private static final String API_KEY = "AIzaSyAe4lO-p73NJHMYNJjrQCaNXT01HkUb7Mo";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_surakshabott);

        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewMessages);
        editText = findViewById(R.id.editTextMessage);
        ImageButton sendButton = findViewById(R.id.buttonSend);

        messageAdapter = new MessAdaptor(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener(v -> {
            String question = editText.getText().toString().trim();
            if (!question.isEmpty()) {
                addToChat(question, Message.SENT_BY_ME);
                editText.setText("");

                if (handleCustomCommands(question)) return;

                // Show Typing Indicator
                addToChat("Typing...", Message.SENT_BY_BOT);
                callAPI(question);
            } else {
                Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    private boolean handleCustomCommands(String message) {
        message = message.toLowerCase();

        if (message.contains("your name") || message.contains("who are you") ||
                message.contains("what are you") || message.contains("who made you")) {
            addToChat("I am Suraksha AI, designed by HarTan.", Message.SENT_BY_BOT);
            return true;
        }

        if (message.contains("locate") || message.contains("find") || message.contains("safe zone")) {
            extractLocationAndOpenMaps(message);
            return true;
        } else if (message.equalsIgnoreCase("open youtube")) {
            openApp("com.google.android.youtube");
            return true;
        } else if (message.equalsIgnoreCase("open whatsapp")) {
            openApp("com.whatsapp");
            return true;
        } else if (message.equalsIgnoreCase("open facebook")) {
            openApp("com.facebook.katana");
            return true;
        } else if (message.equalsIgnoreCase("open instagram")) {
            openApp("com.instagram.android");
            return true;
        }

        return false;
    }

    private void extractLocationAndOpenMaps(String message) {
        String location = message.replaceAll(".*?(?:locate|find|safe zone) (.*)", "$1");
        if (!location.equals(message)) {
            openGoogleMaps(location);
        } else {
            openGoogleMaps("Police Station Near Me");
        }
    }

    private void openGoogleMaps(String query) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(query)));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void openApp(String packageName) {
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void callAPI(String question) {
        try {
            // ✅ Corrected JSON payload for Gemini API
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("contents", new JSONArray()
                    .put(new JSONObject()
                            .put("role", "user")
                            .put("parts", new JSONArray()
                                    .put(new JSONObject().put("text", question)))));

            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + API_KEY;

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("API_ERROR", "API Call Failed", e);
                    runOnUiThread(() -> {
                        messageList.remove(messageList.size() - 1); // Remove "Typing..."
                        addToChat("Failed to get response", Message.SENT_BY_BOT);
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful() || response.body() == null) {
                        Log.e("API_ERROR", "Response Unsuccessful: " + response);
                        return;
                    }

                    String responseData = response.body().string();
                    Log.d("API_RESPONSE", responseData);

                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        JSONArray candidates = jsonResponse.optJSONArray("candidates");
                        if (candidates != null && candidates.length() > 0) {
                            JSONObject firstCandidate = candidates.getJSONObject(0);
                            JSONObject content = firstCandidate.optJSONObject("content");
                            if (content != null) {
                                JSONArray parts = content.optJSONArray("parts");
                                if (parts != null && parts.length() > 0) {
                                    String botReply = parts.getJSONObject(0).optString("text", "").trim();
                                    if (!botReply.isEmpty()) {
                                        runOnUiThread(() -> {
                                            messageList.remove(messageList.size() - 1); // Remove "Typing..."
                                            addToChat(botReply, Message.SENT_BY_BOT);
                                        });
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("API_ERROR", "Error parsing response", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("API_ERROR", "Exception in callAPI", e);
        }
    }
}
