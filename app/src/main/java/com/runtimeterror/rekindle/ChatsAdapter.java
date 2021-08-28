package com.runtimeterror.rekindle;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.accounttransfer.AccountTransfer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int OTHERS_MESSAGE = 0;
    private static int MY_MESSAGE = 1;
    private static int OTHERS_FLASHCARD = 2;
    private static int MY_FLASHCARD = 3;
    private List<RekindleMessage> messageList;
    private DBhelper db = new DBhelper();

    public ChatsAdapter(List<RekindleMessage> messageList) {
        this.messageList = messageList;
    }

    public static class OthersMessageViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView profilePic;
        private TextView senderName;
        private TextView messageContent;
        public OthersMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.imageProfile);
            senderName = itemView.findViewById(R.id.sender_name);
            messageContent = itemView.findViewById(R.id.textMessage);
        }
    }

    public static class MyMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageContent;
        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageContent = itemView.findViewById(R.id.textMessage);
        }
    }

    public static class OthersFlashCardViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView profilePic;
        private TextView senderName, question, answer;
        public OthersFlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.imageProfile);
            senderName = itemView.findViewById(R.id.sender_name);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
        }
    }

    public static class MyFlashCardViewHolder extends RecyclerView.ViewHolder {
        private TextView question, answer;
        public MyFlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == OTHERS_MESSAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_container_received_message, parent, false);
            return new OthersMessageViewHolder(view);
        } else if (viewType == MY_MESSAGE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_container_sent_message, parent, false);
            return new MyMessageViewHolder(view);
        } else if (viewType == OTHERS_FLASHCARD) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_flashcards_received, parent, false);
            return new OthersFlashCardViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard_sent, parent, false);
        return new MyFlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RekindleMessage message = messageList.get(position);
        if (holder.getItemViewType() == OTHERS_MESSAGE) {
            bindOthersMessage(holder, message);
        } else if (holder.getItemViewType() == MY_MESSAGE){
            bindMyMessage(holder, message);
        } else if (holder.getItemViewType() == OTHERS_FLASHCARD) {
            bindOthersFlashcard(holder, message);
        } else if (holder.getItemViewType() == MY_FLASHCARD) {
            bindMyFlashcard(holder, message);
        }
    }

    private void bindOthersMessage(RecyclerView.ViewHolder holder, RekindleMessage message) {
        OthersMessageViewHolder othersMessageViewHolder = (OthersMessageViewHolder) holder;
        db.getUserDocRef(message.getSentBy())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                            new DownloadSpriteTask(othersMessageViewHolder.profilePic).execute(userInfo.getPhotoURL());
                            othersMessageViewHolder.senderName.setText(userInfo.getUsername());
                        }
                    }
                });
        othersMessageViewHolder.messageContent.setText(message.getMessage());
    }

    private void bindMyMessage(RecyclerView.ViewHolder holder, RekindleMessage message) {
        MyMessageViewHolder myMessageViewHolder = (MyMessageViewHolder) holder;
        myMessageViewHolder.messageContent.setText(message.getMessage());
    }

    private void bindOthersFlashcard(RecyclerView.ViewHolder holder, RekindleMessage message) {
        OthersFlashCardViewHolder othersFlashCardViewHolder = (OthersFlashCardViewHolder) holder;
        db.getUserDocRef(message.getSentBy())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                            new DownloadSpriteTask(othersFlashCardViewHolder.profilePic).execute(userInfo.getPhotoURL());
                            othersFlashCardViewHolder.senderName.setText(userInfo.getUsername());
                        }
                    }
                });
        othersFlashCardViewHolder.question.setText(message.getQuestion());
        othersFlashCardViewHolder.answer.setText(message.getAnswer());
    }

    private void bindMyFlashcard(RecyclerView.ViewHolder holder, RekindleMessage message) {
        MyFlashCardViewHolder myFlashCardViewHolder = (MyFlashCardViewHolder) holder;
        myFlashCardViewHolder.question.setText(message.getQuestion());
        myFlashCardViewHolder.answer.setText(message.getAnswer());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        RekindleMessage message = messageList.get(position);
        if (message.getSentBy().equals(db.getUser().getUid())) {
            if (message.getType() == Constants.TYPE_MESSAGE)
                return MY_MESSAGE;
            else if (message.getType() == Constants.TYPE_FLASHCARD)
                return MY_FLASHCARD;
        } else {
            if (message.getType() == Constants.TYPE_MESSAGE)
                return OTHERS_MESSAGE;
            else if (message.getType() == Constants.TYPE_FLASHCARD)
                return OTHERS_MESSAGE;
        }
        return -1;
    }
}
