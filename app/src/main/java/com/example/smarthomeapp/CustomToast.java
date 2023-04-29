package com.example.smarthomeapp;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast extends Toast {
    public static int SUCCESS = 1;
    public static int WARNING = 2;
    public static int ERROR = 3;
    public static int CONFUSING = 4;

    private static long SHORT = 4000;
    private static long LONG = 7000;
    public CustomToast(Context context) {
        super(context);
    }


    public static Toast toastCenterTransparent(Context context, String message, int duration, int type) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);

        View layout = LayoutInflater.from(context).inflate(R.layout.toast_cart_layout, null, false);
        TextView l1 = (TextView) layout.findViewById(R.id.toast_text);
        ImageView icon = (ImageView) layout.findViewById(R.id.toast_icon);
        l1.setText(message);

        if (type == 1) {
            icon.setImageResource(R.drawable.ic_baseline_check_24);
        }
        else {
            icon.setImageResource(R.drawable.ic_error_outline_24);
        }

        toast.setView(layout);
        return toast;
    }

}