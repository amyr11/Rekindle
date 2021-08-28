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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == OTHERS_MESSAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_container_received_message, parent, false);
            return new OthersMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_container_sent_message, parent, false);
            return new MyMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RekindleMessage message = messageList.get(position);
        if (holder.getItemViewType() == OTHERS_MESSAGE) {
            bindOthersMessage(holder, message);
        } else {
            bindMyMessage(holder, message);
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

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (messageList.get(position).getSentBy().equals(db.getUser().getUid())) {
            return MY_MESSAGE;
        } else {
            return OTHERS_MESSAGE;
        }
    }
}
