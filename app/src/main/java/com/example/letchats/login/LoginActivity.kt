package com.example.letchats.login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.letchats.BaseActivity
import com.example.letchats.ChatApplication.getStorage
import com.example.letchats.ProfileActivity
import com.example.letchats.R
import com.example.letchats.extension.redirectToProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.login_activity.*


class LoginActivity : BaseActivity() {


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1
    lateinit var gso: GoogleSignInOptions
    private lateinit var mAuth: FirebaseAuth


    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        mAuth = FirebaseAuth.getInstance()
        createRequest()
        //Click on sign in button
        google_sign_button.setOnClickListener {
            signIn()
        }

    }


    private fun createRequest() {
        // Configure Google Sign In
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        Log.d(TAG, "gson--- $gso")

        //firebase auth instance

    }

    private fun signIn() {

        mAuth = FirebaseAuth.getInstance()
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)


        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
              //  getStorage().storeIsUserLoginStatus(true)
                val userEmail = account.email.toString()
              //  getStorage().storeUserEmail(userEmail)
                val userName = account.displayName.toString()
                val userPhoto = account.photoUrl
                Log.d(ContentValues.TAG, "details: $userEmail, $userName, $userPhoto")
                saveFireStore(userEmail, userName, userPhoto)
                redirectToProfileActivity()
            } else {

            }
        }
    }

    private fun saveFireStore(email: String?, name: String?, photo: Uri?) {

        if (email != null && name != null && photo != null) {
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            val user = HashMap<String, Any>()
            user["email"] = email.toString()
            user["name"] = name.toString()
            user["photo"] = photo.toString()
            db.collection("users")
                .add(user)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "record added successfully: ${it.id}")
                }
                .addOnFailureListener {
                    Log.d(ContentValues.TAG, "record Failed  ")
                }
        }


    }
}
