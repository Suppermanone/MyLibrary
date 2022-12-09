package com.zdww.mylibrary;

import android.content.Context;
import android.widget.Toast;

public class Demo {
    public static void showToast(Context context) {
        Toast.makeText(context, "toast toast!", Toast.LENGTH_SHORT).show();
    }
}
