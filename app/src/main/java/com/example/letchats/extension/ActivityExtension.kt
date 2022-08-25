package com.example.letchats.extension

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.letchats.ProfileActivity
import com.example.letchats.login.LoginActivity

import java.io.File


fun AppCompatActivity.redirectToProfileActivity() {
    val intent = Intent(this, ProfileActivity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.redirectToLoginActivity(){
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    this.finish()
}



fun setToolbarProfileImage(
    activity: Activity,
    imageView: ImageView?,
) {
    val imagePath = File(activity.getExternalFilesDir(null), "/ProfileImage")
    try {
        val imageFile = File(imagePath.path + "/" )
        Log.d("image path", "setImage: " + imagePath.path + "/" )
        if (imageFile.exists()) {
            val mBitmap = BitmapFactory.decodeFile(imagePath.path + "/")
            val rotatedBitmap =
                Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.width, mBitmap.height, null, true)
            //imageView.visibility = View.VISIBLE
            //textView.visibility = View.GONE
            imageView?.background = null
            imageView?.setImageBitmap(rotatedBitmap)
        } else {
            //splitFirstLetterFromWord(textView, CustomerInfoHelper().getFirstName() + " " + CustomerInfoHelper().getLastName())
        }
    } catch (exc: Exception) {
        exc.printStackTrace()
    }
}