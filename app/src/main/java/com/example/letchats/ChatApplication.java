package com.example.letchats;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;




/**
 * Created by swift on 8/2/17.
 */

public class ChatApplication extends MultiDexApplication {

    private static Preferences ChatPreference = null;

    private static ChatApplication instance;

    public ChatApplication() {
        instance = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ChatPreference = new Preferences();


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }*/
    }



    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Preferences getStorage() {
        return ChatPreference;
    }

    public static ChatApplication getContext() {
        return instance;
    }







    }


