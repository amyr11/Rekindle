package com.runtimeterror.rekindle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThreadsAdapter extends RecyclerView.Adapter<ThreadsAdapter.ThreadViewHolder> {
    public static class ThreadViewHolder extends RecyclerView.ViewHolder{
        private CardView container;
        private TextView threadName, memberCount, reviewButton;
        public ThreadViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.thread_container);
            threadName = itemView.findViewById(R.id.threadName);
            memberCount = itemView.findViewById(R.id.member_count);
            reviewButton = itemView.findViewById(R.id.button_review);
        }
    }

    private List<RekindleThread> threadList;
    public ThreadsAdapter(List<RekindleThread> threadList) {
        this.threadList = threadList;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position) {
        RekindleThread current = threadList.get(position);
        holder.container.setCardBackgroundColor(
                ViewUtils.getCardColor(holder.container.getContext(), current.getTheme())
        );
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.container.getContext();
                Intent intent = new Intent(context, ThreadGroupChat.class);
                intent.putExtra("threadName", ViewUtils.limitChars(current.getName(), 15));
                intent.putExtra("threadID", current.getId());
                context.startActivity(intent);
            }
        });
        holder.threadName.setText("# " + current.getName());
        int memberCount = current.getMemberCount();
        String membersString =  memberCount > 1 ? memberCount + " Members" : memberCount + " Member";
        holder.memberCount.setText(membersString);
        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open review thread activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }
}
