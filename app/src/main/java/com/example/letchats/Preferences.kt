package com.example.letchats


import android.content.Context
import android.content.SharedPreferences
import com.example.letchats.ChatApplication.getContext
import java.util.prefs.Preferences


class Preferences {

    private val TAG: String = Preferences::class.java.simpleName

    private val preferences: SharedPreferences =
        getContext().getSharedPreferences("Chat_App", Context.MODE_PRIVATE)


    fun storeIsUserLoginStatus(status: Boolean) {
        preferences.edit().putBoolean("Auth_Log_Out", false).apply()
    }

    fun getIsUserLoginStatus(): Boolean {
        return preferences.getBoolean("Auth_Log_Out", true)
    }

    fun storeUserEmail(userName: String) {
        preferences.edit().putString("User_Email", userName).apply()
    }
    fun getUserEmail() {
        preferences.edit().putString("User_Email", "").apply()
    }


}

