package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ThreadGroupChat extends AppCompatActivity {
    private DBhelper db = new DBhelper();
    private RecyclerView chatsRecyclerView;
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
        loadThread();
    }

    private void viewsInit() {
        ViewUtils.setHeaderChats(this, currentThread);
    }

    private void recyclerViewInit() {
        //TODO
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
    }

    public static void allowReferesh() {
        allowReferesh = true;
    }
}