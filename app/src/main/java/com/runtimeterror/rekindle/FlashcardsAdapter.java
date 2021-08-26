package com.runtimeterror.rekindle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsAdapter extends RecyclerView.Adapter<FlashcardsAdapter.FlashcardViewHolder> {
    private List<Flashcard> flashcardList = new ArrayList<>();
    private String collectionID;
    private String userID;

    public FlashcardsAdapter(List<Flashcard> flashcards, String collectionID, String userID) {
        this.flashcardList = flashcards;
        this.collectionID = collectionID;
        this.userID = userID;
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        private ImageView editButton, removeButton;
        private TextView questionTextView, answerTextView;
        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.pencil);
            removeButton = itemView.findViewById(R.id.remove);
            questionTextView = itemView.findViewById(R.id.fTitle);
            answerTextView = itemView.findViewById(R.id.fDesc);
        }
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard current = flashcardList.get(position);
        holder.questionTextView.setText(current.getQuestion());
        holder.answerTextView.setText(current.getAnswer());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.editButton.getContext();
                Intent intent = new Intent(context, EditFlashcard.class);
                intent.putExtra("userID", userID);
                intent.putExtra("collectionID", collectionID);
                intent.putExtra("flashcardID", current.getId());
                intent.putExtra("question", current.getQuestion());
                intent.putExtra("answer", current.getAnswer());
                context.startActivity(intent);
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFlashcard(current, holder.getBindingAdapterPosition());
            }
        });
    }

    private void removeFlashcard(Flashcard flashcard, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.COL_USERS)
                .document(userID)
                .collection(Constants.COL_FLASHCARD_COLLECTIONS)
                .document(collectionID)
                .collection(Constants.COL_FLASHCARD_LIST)
                .document(flashcard.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG, "Flashcard successfully deleted.");
                        } else {
                            Log.w(Constants.TAG, "Flashcard deletion failed.", task.getException());
                        }
                    }
                });
        flashcardList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }
}
