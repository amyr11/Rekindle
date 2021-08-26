package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class EditFlashcard extends CreateFlashcard {
    private String flashcardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard);
        viewsInit();
    }

    @Override
    protected void viewsInit() {
        super.viewsInit();
        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");
        questionEditText.setText(question);
        answerEditText.setText(answer);
    }

    @Override
    protected void onSave(String question, String answer) {
        updateFlashcard(question, answer);
    }

    private void updateFlashcard(String question, String answer) {
        Map<String, Object> flashcard = new HashMap<>();
        flashcard.put("question", question);
        flashcard.put("answer", answer);

        flashcardID = getIntent().getStringExtra("flashcardID");
        db.collection(Constants.COL_USERS)
                .document(userID)
                .collection(Constants.COL_FLASHCARD_COLLECTIONS)
                .document(collectionID)
                .collection(Constants.COL_FLASHCARD_LIST)
                .document(flashcardID)
                .update(flashcard)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Flashcard successfully updated.");
                        } else {
                            Log.w(Constants.TAG, "Flashcard update failed.", task.getException());
                        }
                    }
                });
    }
}