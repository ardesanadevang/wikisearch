package com.example.wikisearch.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Devang on 01/01/2017.
 */

public class Util {

    public static void putInLog(String message) {
        Log.w("Logg", "" + message);
    }

    public static void showToast(Activity activity, String message) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }
}

