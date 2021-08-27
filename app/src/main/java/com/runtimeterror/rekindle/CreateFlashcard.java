package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.protobuf.TimestampProto;

import java.util.Date;

public class CreateFlashcard extends AppCompatActivity {
    protected DBhelper db = new DBhelper();
    protected EditText questionEditText, answerEditText;
    protected TextView saveButton, cancelButton;
    protected String collectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard_personal);
        collectionID = getIntent().getStringExtra("collectionID");
        viewsInit();
    }

    //Override for thread
    protected void viewsInit() {
        questionEditText = findViewById(R.id.question);
        answerEditText = findViewById(R.id.answer);
        saveButton = findViewById(R.id.button_save);
        cancelButton = findViewById(R.id.button_cancel);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEditText.getText().toString();
                String answer = answerEditText.getText().toString();
                if (!question.isEmpty() && !answer.isEmpty()) {
                    onSave(question, answer);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "All fields must be filled.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onSave(String question, String answer) {
        Flashcard flashcard = new Flashcard(question, answer, new Date());
        addToCollection(flashcard);
    }

    //Override for thread
    private CollectionReference getSubCollection() {
        return db.getFlashcardListColRef(collectionID);
    }

    private void addToCollection(Flashcard flashcard) {
        getSubCollection()
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
}