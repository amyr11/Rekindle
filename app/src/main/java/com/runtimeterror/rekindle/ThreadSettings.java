package com.runtimeterror.rekindle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadSettings extends AppCompatActivity {
    private TextView copyThreadCode, viewMembers, leaveThread, closeThread;
    private String threadID;

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
        closeThread = findViewById(R.id.close_thread);
    }
}