package com.runtimeterror.rekindle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    public class MemberViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        private ImageView profilePic;
        private TextView memberName;
        private ImageButton optionButton;
        private UserInfo member;
        private String threadID;
        private int position;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_pic);
            memberName = itemView.findViewById(R.id.member_name);
            optionButton = itemView.findViewById(R.id.button_more);
            optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    member = (UserInfo) v.getTag(R.string.currentMember);
                    threadID = (String) v.getTag(R.string.threadID);
                    position = getAbsoluteAdapterPosition();
                    showMemberOption(v);
                }
            });
        }

        public void showMemberOption(View view) {
            PopupMenu taskOption = new PopupMenu(view.getContext(), view);
            taskOption.setOnMenuItemClickListener(this);
            taskOption.inflate(R.menu.option_member);
            taskOption.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.kick_member) {
                new AlertDialog.Builder(itemView.getContext())
                        .setMessage("Kick " + member.getUsername() + "?")
                        .setPositiveButton("Cancel", null)
                        .setNegativeButton("Kick", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                kickMember();
                            }
                        })
                        .show();
                return true;
            }
            return false;
        }

        private void kickMember() {
            db.removeMemberFromThread(
                    threadID,
                    member.getUserID(),
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //called when the userID has been removed from the thread's member array
                            if (task.isSuccessful()) {
                                Log.d(Constants.TAG, "User successfully removed from thread");
                            } else {
                                Log.w(Constants.TAG, "Failed to remove user from thread", task.getException());
                            }
                        }
                    },
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //called when the threadID has been removed from the user's threads array
                            if (task.isSuccessful()) {
                                Log.d(Constants.TAG, "Thread successfully removed from user's threads array");
                            } else {
                                Log.d(Constants.TAG, "Failed to remove thread from user's threads array");
                            }
                        }
                    }
            );
            memberList.remove(position);
            notifyItemRemoved(position);
            ThreadsFragment.allowRefresh();
        }
    }

    private List<UserInfo> memberList;
    private String threadID;
    private boolean isOwner;
    private DBhelper db;
    public MembersAdapter(String threadID, boolean isOwner, List<UserInfo> members) {
        this.threadID = threadID;
        this.memberList = members;
        this.isOwner = isOwner;
        this.db = new DBhelper();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        UserInfo current = memberList.get(position);
        new DownloadSpriteTask(holder.profilePic).execute(current.getPhotoURL());
        holder.memberName.setText(current.getUsername());
        if (isOwner && !current.getUserID().equals(db.getUser().getUid())) {
            holder.optionButton.setVisibility(View.VISIBLE);
            holder.optionButton.setTag(R.string.currentMember, current);
            holder.optionButton.setTag(R.string.threadID, threadID);
        } else {
            holder.optionButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
