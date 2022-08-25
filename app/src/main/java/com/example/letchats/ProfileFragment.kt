package com.example.letchats


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.example.letchats.databinding.FragmentProfileBinding
import com.example.letchats.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.reflect.Array.set


class ProfileFragment : AppCompatActivity() {

    // declare the GoogleSignInClient
    lateinit var mGoogleSignInClient: GoogleSignInClient


    // val auth is initialized by lazy

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_profile)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
        // setContentView(R.layout.profile_acitivity)
     //  clickEvent()

    }



    private fun clickEvent()
    {
        log_out.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                //ChatApplication.getStorage().storeIsUserLoginStatus(false)
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                this.finish()

            }
        }
    }





}