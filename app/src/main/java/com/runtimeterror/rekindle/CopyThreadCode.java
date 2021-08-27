package com.runtimeterror.rekindle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CopyThreadCode extends AppCompatActivity {
    private TextView threadCode, copyButton, dismissButton;
    private String threadCodeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_thread_code);
        Toast.makeText(getApplicationContext(), "Thread created.", Toast.LENGTH_SHORT).show();
        threadCodeString = getIntent().getStringExtra("threadCode");
        threadCode = findViewById(R.id.thread_code);
        threadCode.setText(threadCodeString);
        copyButton = findViewById(R.id.copyButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Rekindle thread join code", threadCodeString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Code copied to clipboard.", Toast.LENGTH_SHORT).show();
            }
        });
        dismissButton = findViewById(R.id.dismiss);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}