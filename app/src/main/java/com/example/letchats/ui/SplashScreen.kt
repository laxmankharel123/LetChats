package com.example.letchats.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.letchats.ProfileActivity
import com.example.letchats.R
import com.example.letchats.login.LoginActivity
class SplashScreen: AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        handleUserDirection()

    }

    private fun handleUserDirection(){
        Handler().postDelayed({
            val sharedPreferences = getSharedPreferences("MyLoginPref", MODE_PRIVATE)
            val shareStatus: Boolean = sharedPreferences.getBoolean("LoginStatus", false)
            if (shareStatus) {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }, 500)
    }

}


