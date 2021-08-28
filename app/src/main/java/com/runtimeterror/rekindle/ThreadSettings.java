package com.runtimeterror.rekindle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ThreadSettings extends AppCompatActivity {
    private TextView copyThreadCode, viewMembers, leaveThread, closeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_settings);
        ViewUtils.setHeader(this, "Thread menu");
        copyThreadCode = findViewById(R.id.copy_thread_code);
        viewMembers = findViewById(R.id.view_members);
        leaveThread = findViewById(R.id.leave_thread);
        closeThread = findViewById(R.id.close_thread);
    }
}