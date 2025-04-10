package com.example.tangthucac.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tangthucac.OpenAIHelper;
import com.example.tangthucac.R;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.tangthucac.adapter.ChatAdapter;
import com.example.tangthucac.model.Message;

import okhttp3.*;
import org.jetbrains.annotations.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private EditText edtMessage;
    private Button btnSend;
    private ChatAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecycler = findViewById(R.id.chatRecycler);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new ChatAdapter(messageList);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String question = edtMessage.getText().toString();
            if (!question.isEmpty()) {
                addMessage(new Message(question, "user"));
                OpenAIHelper.sendQuestion(question, new Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            String answer = OpenAIHelper.parseGeminiResponse(response.body().string());
                            runOnUiThread(() -> addMessage(new Message(answer, "bot")));
                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }
                });
                edtMessage.setText("");
            }
        });
    }

    private void addMessage(Message msg) {
        messageList.add(msg);
        adapter.notifyItemInserted(messageList.size() - 1);
        chatRecycler.scrollToPosition(messageList.size() - 1);
    }
}