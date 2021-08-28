package com.runtimeterror.rekindle;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThreadGroupChat extends AppCompatActivity {
    private DBhelper db = new DBhelper();
    private RecyclerView chatsRecyclerView;
    private ChatsAdapter chatsAdapter;
    private LinearLayoutManager layoutManager;

    private ImageButton sendFlashcardButton, sendMessageButton;
    private EditText chatInput;
    private String threadID;
    private RekindleThread currentThread;
    private static boolean allowReferesh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_group_chat);

        threadID = getIntent().getStringExtra("threadID");
        sendFlashcardButton = findViewById(R.id.button_send_flashcard);
        sendMessageButton = findViewById(R.id.send_message);
        chatInput = findViewById(R.id.inputMessage);
        chatsRecyclerView =findViewById(R.id.chats_recyclerview);
        loadThread();
        recyclerViewInit();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatInput.getText().toString();
                if (!message.isEmpty()) {
                    sendMessage(db.getUser().getUid(), message);
                    chatInput.setText("");
                }
            }
        });

        sendFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = sendFlashcardButton.getContext();
                Intent intent = new Intent(context, CreateFlashcardThread.class);
                intent.putExtra("threadID", threadID);
                context.startActivity(intent);
            }
        });
    }

    private void viewsInit() {
        ViewUtils.setHeaderChats(this, currentThread);
    }

    private void recyclerViewInit() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatsRecyclerView.setLayoutManager(layoutManager);
    }

    private void sendMessage(String sentBy, String message) {
        RekindleMessage rekindleMessage = new RekindleMessage(
                new Date(),
                sentBy,
                message,
                0
        );
        db.getMessagesColRef(threadID)
                .add(rekindleMessage)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Message sent!");
                        } else {
                            Log.w(Constants.TAG, "Message not sent :(", task.getException());
                        }
                    }
                });
    }

    private void loadChats() {
        List<RekindleMessage> messageList = new ArrayList<>();
        db.getMessagesColRef(threadID)
                .orderBy(Constants.FIELD_SENT_AT, Query.Direction.ASCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(Constants.TAG, "Chat listener error.", error);
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == ADDED) {
                                RekindleMessage message = dc.getDocument().toObject(RekindleMessage.class);
                                messageList.add(message);
                            }
                        }
                        chatsAdapter = new ChatsAdapter(messageList);
                        chatsRecyclerView.setAdapter(chatsAdapter);
                        chatsRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    private void loadThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db.getThreadDocRef(threadID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    currentThread = task.getResult().toObject(RekindleThread.class);
                                    currentThread.setId(doc.getId());
                                    viewsInit();
                                    Log.d(Constants.TAG, "Thread info loaded successfully");
                                }
                            }
                        });
            }
        }, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (allowReferesh) {
            loadThread();
        }
        loadChats();
    }

    public static void allowReferesh() {
        allowReferesh = true;
    }
}