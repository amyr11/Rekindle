package com.runtimeterror.rekindle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ThreadSettings extends AppCompatActivity {
    private TextView copyThreadCode, viewMembers, leaveThread, closeThread;
    private String threadID;
    private DBhelper db = new DBhelper();
    private boolean removedUserFromThread = false, removedThreadFromUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_settings);
        threadID = getIntent().getStringExtra("threadID");
        ViewUtils.setHeader(this, "Thread menu");
        copyThreadCode = findViewById(R.id.copy_thread_code);
        copyThreadCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Rekindle thread join code", threadID);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Code copied to clipboard.", Toast.LENGTH_SHORT).show();
            }
        });
        viewMembers = findViewById(R.id.view_members);
        viewMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = viewMembers.getContext();
                Intent intent = new Intent(context, MembersPanel.class);
                intent.putExtra("threadID", threadID);
                context.startActivity(intent);
            }
        });
        leaveThread = findViewById(R.id.leave_thread);
        leaveThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(leaveThread.getContext())
                        .setTitle("Leave Thread")
                        .setMessage("Are you sure you want to leave this thread?")
                        .setPositiveButton("Cancel", null)
                        .setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                leaveThread();
                            }
                        })
                        .show();
            }
        });
        closeThread = findViewById(R.id.close_thread);
        closeThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: close thread
            }
        });
    }

    private void leaveThread() {
        db.removeMemberFromThread(
                threadID,
                db.getUser().getUid(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            removedUserFromThread = true;
                            if (removedThreadFromUser)
                                finishActivities();
                        }
                    }
                },
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            removedThreadFromUser = true;
                            if (removedUserFromThread)
                                finishActivities();
                        }
                    }
                }
        );
    }

    private void finishActivities() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ThreadsFragment.allowRefresh();
        startActivity(intent);
    }
}