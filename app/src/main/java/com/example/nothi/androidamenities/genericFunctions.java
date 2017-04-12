package com.example.nothi.androidamenities;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by allenhsu on 2017-04-05.
 */

public class genericFunctions {
    public static String BOOLEAN_TRUE = "true";
    public static String BOOLEAN_FALSE = "false";

    public static void createSnackbar(View v, String message, int duration) {
        Snackbar.make(v, message, duration).setAction("Action", null).show();
    }
}