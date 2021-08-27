package com.runtimeterror.rekindle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private Context context;

    public FlashcardsAdapter(Context context, List<Flashcard> flashcards, String collectionID) {
        this.context = context;
        this.flashcardList = flashcards;
        this.collectionID = collectionID;
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
                new AlertDialog.Builder(context)
                        .setTitle("Delete flashcard")
                        .setMessage("Are you sure you want to delete this flashcard? This cannot be undone.")
                        .setPositiveButton("Cancel", null)
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeFlashcard(current, holder.getBindingAdapterPosition());
                            }
                        })
                        .show();
            }
        });
    }

    private void removeFlashcard(Flashcard flashcard, int position) {
        DBhelper db = new DBhelper();
        db.getFlashcardDocRef(collectionID, flashcard.getId())
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
