package com.pheonix.org.hallaka.Utils;

import android.app.Activity;
import android.view.WindowManager;

public class Funcs {
    public static void disableCurrentScreen(Activity act){
        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    public static void enableCurrentScreen(Activity act){
        act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
