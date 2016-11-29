package com.btranz.ecommerceapp.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by all on 8/31/2016.
 */
public class AppData extends Application {
    public static boolean currentFlag;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
