package com.runtimeterror.rekindle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

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

    public static TextView getHeaderTextButton(Activity activity, String text) {
        TextView textButton = activity.findViewById(R.id.text_button);
        textButton.setText(text);
        return textButton;
    }

    public static int getCardColor(Context context, int theme) {
        switch (theme) {
            default:
            case 0:
                return ContextCompat.getColor(context, R.color.orange_light);
            case 1:
                return ContextCompat.getColor(context, R.color.purple_light);
            case 2:
                return ContextCompat.getColor(context, R.color.orange_yellow);
            case 3:
                return ContextCompat.getColor(context, R.color.red);
            case 4:
                return ContextCompat.getColor(context, R.color.pink);
        }
    }

    public static int getAbbrColor(Context context, int theme) {
        switch (theme) {
            default:
            case 0:
                return ContextCompat.getColor(context, R.color.orange_light_2);
            case 1:
                return ContextCompat.getColor(context, R.color.purple_light_2);
            case 2:
                return ContextCompat.getColor(context, R.color.orange_yellow_2);
            case 3:
                return ContextCompat.getColor(context, R.color.red_2);
            case 4:
                return ContextCompat.getColor(context, R.color.pink_2);
        }
    }

    public static String limitChars(String string, int limit) {
        if (string.length() > limit) {
            return string.substring(0, limit) + "\n...";
        }
        return string;
    }
}
