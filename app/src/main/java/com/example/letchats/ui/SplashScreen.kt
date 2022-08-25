package com.example.letchats.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.letchats.R
import com.example.letchats.extension.redirectToLoginActivity
import com.example.letchats.utils.MyPrefernce
import com.example.letchats.utils.SharedViewModel
import com.example.letsConnect.utils.UserUtils

class SplashScreen: AppCompatActivity() {


    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        UserUtils.updatePushToken()
        sharedViewModel.onFromSplash()
        sharedViewModel.openMainAct.observe(this) {
            redirectToLoginActivity()
        }

    }

}


