package com.runtimeterror.rekindle;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

public class ViewUtils {
    public static void setHeader(Activity activity, String title) {
        ImageButton backButton = activity.findViewById(R.id.button_back);
        TextView headerTitle = activity.findViewById(R.id.header_title);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        headerTitle.setText(title);
    }

    public static SwitchCompat getSwitchView(Activity activity, int viewId, String title) {
        View parent = activity.findViewById(viewId);
        TextView titleView = parent.findViewById(R.id.title);
        titleView.setText(title);
        return parent.findViewById(R.id.switch_view);
    }
}
