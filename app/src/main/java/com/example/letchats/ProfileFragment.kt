package com.example.letchats


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.letchats.MyAdapter.bindMenuImageFromUrl
import com.example.letchats.databinding.FragmentProfileBinding
import com.example.letchats.extension.redirectToProfileActivity
import com.example.letchats.login.LoginActivity
import com.example.letchats.login.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*



class ProfileFragment : AppCompatActivity() {

    // declare the GoogleSignInClient
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth


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
       clickEvent()
        bindData()

    }

    private fun bindData(){
        mAuth = FirebaseAuth.getInstance()
        binding.emailName.text = mAuth.currentUser!!.email
        binding.textName.text = mAuth.currentUser!!.displayName
      //  binding.textPhone.text = mAuth.currentUser!!.phoneNumber

        bindMenuImageFromUrl(
            image as AppCompatImageView,
            mAuth.currentUser!!.photoUrl.toString()
        )

        binding.arrowBack.setOnClickListener {
            redirectToProfileActivity()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        redirectToProfileActivity()
    }





    private fun clickEvent()
    {
        log_out.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                //ChatApplication.getStorage().storeIsUserLoginStatus(false)
                val sharedPreferences = getSharedPreferences("MyLoginPref", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean("LoginStatus", false)
                    apply()
                }
                editor.apply {
                    putBoolean("DataStatus", true)
                    apply()
                }
                val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }






