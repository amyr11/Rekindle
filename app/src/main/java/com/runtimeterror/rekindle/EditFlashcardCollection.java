package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class EditFlashcardCollection extends CreateFlashcardCollection {
    private String collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard_collection);
        viewsInit();
    }

    @Override
    protected void viewsInit() {
        super.viewsInit();
        String collectionName = getIntent().getStringExtra("collectionName");
        colNameEditText.setText(collectionName);
    }

    @Override
    protected void onSave(String titleFull) {
        updateCollection(titleFull);
        HomeFragment.allowRefresh();
        finish();
    }

    private void updateCollection(String titleFull) {
        Map<String, Object> collection = new HashMap<>();
        collection.put(FlashcardCollection.FCOLLECTION_FULL_TITLE, titleFull);
        collection.put(FlashcardCollection.FCOLLECTION_ABBR_TITLE, FlashcardCollection.generateAbbr(titleFull));

        collectionId = getIntent().getStringExtra("collectionID");
        db.getFlashcardCollectionDocRef(collectionId)
                .update(collection)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Collection updated");
                        } else {
                            Log.w(Constants.TAG, "Collection update failed.", task.getException());
                        }
                    }
                });
    }
}