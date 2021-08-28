package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class CreateFlashcardThread extends CreateFlashcard {
    private EditText decoy1, decoy2;
    private String threadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard_thread);
        viewsInit();
    }

    @Override
    protected CollectionReference getSubCollection() {
        threadID = getIntent().getStringExtra("threadID");
        return db.getThreadFlashcardsColRef(threadID);
    }

    @Override
    protected void viewsInit() {
        super.viewsInit();
        decoy1 = findViewById(R.id.decoyAnswer1);
        decoy2 = findViewById(R.id.decoyAnswer2);
    }

    @Override
    protected void onSave(String question, String answer) {
        String decoy1String = decoy1.getText().toString();
        String decoy2String = decoy2.getText().toString();
        ThreadFlashcard flashcard = new ThreadFlashcard(question, answer, new Date(), decoy1String, decoy2String);
        addToThreadCollection(flashcard);
        sendFlashcardAsMessage(flashcard);
    }

    private void addToThreadCollection(ThreadFlashcard flashcard) {
        this.getSubCollection()
                .add(flashcard)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Flashcard successfully added");
                        } else {
                            Log.d(Constants.TAG, "Flashcard cannot be added.", task.getException());
                        }
                    }
                });
    }

    private void sendFlashcardAsMessage(Flashcard flashcard) {
        RekindleMessage rekindleMessage = new RekindleMessage();
        rekindleMessage.setSentAt(new Date());
        rekindleMessage.setSentBy(db.getUser().getUid());
        rekindleMessage.setType(Constants.TYPE_FLASHCARD);
        rekindleMessage.setQuestion(flashcard.getQuestion());
        rekindleMessage.setAnswer(flashcard.getAnswer());

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
}