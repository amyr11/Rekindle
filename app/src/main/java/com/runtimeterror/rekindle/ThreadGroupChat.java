package com.runtimeterror.rekindle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class ThreadGroupChat extends AppCompatActivity {
    private RecyclerView chatsRecyclerView;
    private ImageButton sendFlashcardButton, sendMessageButton;
    private EditText chatInput;
    private String threadName;
    private String threadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_group_chat);

        threadName = getIntent().getStringExtra("threadName");
        threadID = getIntent().getStringExtra("threadID");
        ViewUtils.setHeaderChats(this, "# " + threadName, threadID);
        sendFlashcardButton = findViewById(R.id.button_send_flashcard);
        sendMessageButton = findViewById(R.id.send_message);
        chatInput = findViewById(R.id.inputMessage);
    }
}