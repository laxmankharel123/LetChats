package com.example.letchats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.letchats.MyAdapter.MessagesAdapter
import com.example.letchats.databinding.ActivityChatPersonBinding
import com.example.letchats.login.User
import com.example.letchats.model.LatestMsgModel
import com.example.letchats.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_chat_person.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatPersonActivity : AppCompatActivity() {


    //   private lateinit var messageAdapter: MessagesAdapter
    //  private lateinit var messageList: ArrayList<Message>
    //  private lateinit var mDbRef: DatabaseReference


    var binding: ActivityChatPersonBinding? = null
    var messageAdapter: MessagesAdapter? = null
    private var messageList: ArrayList<Message>? = null
    private var latestMsgList: ArrayList<LatestMsgModel>?  =null
    var senderRoom: String? = null
    var receiverRoom: String? = null
    var mDbRef: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var senderUid: String? = null
    var receiverUid: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatPersonBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        mDbRef = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        messageList = ArrayList()
        latestMsgList = ArrayList()
        val userName = intent.getStringExtra("userName")
        val photo = intent.getStringExtra("photo")
        receiverUid = intent.getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().uid
        Log.d("TAG", "senderUid $senderUid")
        mDbRef = FirebaseDatabase.getInstance()


        Glide.with(this)
            .load(photo)
            .placeholder(R.drawable.ic_chat_logo)
            .transform(CircleCrop())
            .into(binding!!.profileImage)

        // bindImageForSVG(profile_image, photo)
        binding!!.profileName.text = userName

        binding!!.imageView.setOnClickListener {
            finish()
        }

        mDbRef!!.reference.child("Presence").child(receiverUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val status = snapshot.getValue(String::class.java)
                        if (status == "offline") {
                            binding!!.status.visibility = View.GONE
                        } else {
                            binding!!.status.visibility = View.VISIBLE
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


      /*  mDbRef!!.reference.child("chats").child(senderRoom!!).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    latestMsgList!!.clear()
                    for (latestMsgValue in snapshot.children )
                    {
                        val latestMsg: LatestMsgModel? = latestMsgValue.getValue(LatestMsgModel::class.java)
                        latestMsgList!!.add(latestMsg!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
*/
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        messageAdapter = MessagesAdapter(this, messageList, senderRoom!!, receiverRoom!!)
        binding!!.recyclerChat.layoutManager =
            LinearLayoutManager(this@ChatPersonActivity, LinearLayoutManager.VERTICAL, false)
        binding!!.recyclerChat.adapter = messageAdapter

        //logic for adding data to recycelerview


        mDbRef!!.reference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList!!.clear()
                    for (postSnapshot in snapshot.children) {
                        val message: Message? = postSnapshot.getValue(Message::class.java)
                        message!!.messageId = postSnapshot.key
                        messageList!!.add(message)



                    }

                    messageAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        //adding the message to database

        binding!!.buttonSend.setOnClickListener {

            val message: String = edit_message.text.toString()
            val date = Date()
            if (message.isNotEmpty() || message.isNotBlank() ) {
                val messageObject = Message(message, senderUid, date.time)



                val randomKey = mDbRef!!.reference.push().key
                val lastMsgObj = HashMap<String, Any>()
                lastMsgObj["lastMsg"] = messageObject.message!!
                lastMsgObj["lastMsgTime"] = date.time



                mDbRef!!.reference.child("chats").child(senderRoom!!).updateChildren(lastMsgObj)
                mDbRef!!.reference.child("chats").child(receiverRoom!!).updateChildren(lastMsgObj)
                mDbRef!!.reference.child("chats").child(senderRoom!!).child("messages")
                    .child(randomKey!!).setValue(messageObject).addOnSuccessListener {
                        mDbRef!!.reference.child("chats").child(receiverRoom!!).child("messages")
                            .child(randomKey).setValue(messageObject).addOnCompleteListener {


                            }
                    }




                binding!!.editMessage.setText("")

            }

        }


        /*mDbRef!!.reference.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObject)

            }*/


        val handler = Handler()
        binding!!.editMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    mDbRef!!.reference.child("Presence")
                        .child(senderUid!!)
                        .setValue("typing.....")
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed(userStoppedTyping, 1000)

                }

                var userStoppedTyping = Runnable {
                    mDbRef!!.reference.child("Presence")
                        .child(senderUid!!)
                        .setValue("Online")

                }

            })

        supportActionBar!!.setDisplayShowTitleEnabled(false)


        // fireBaseUser = FirebaseAuth.getInstance().currentUser
        // reference = FirebaseFirestore.getInstance().collection("users")


    }


/*
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == 25)
        if (data != null) {
            if (data.data != null) {
                val selectedImage = data.data
                val calendar = Calendar.getInstance()
                val reference = storage!!.reference.child("chats")
                    .child(calendar.timeInMillis.toString() + "")

                reference.putFile(selectedImage!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                            reference!!.downloadUrl.addOnSuccessListener { uri ->
                                val messageTxt: String = binding!!.editMessage.text.toString()
                                val date = Date()
                                val message = Message(messageTxt, senderUid, date.time)
                                message.message = "photo"
                                binding!!.editMessage.setText("")
                                val randomKey = mDbRef!!.reference.push().key
                                val lastMsgObj = HashMap<String, Any>()
                                lastMsgObj["lastMsg"] = message.message!!
                                lastMsgObj["lastMsgTime"] = date.time
                                database!!.reference.child("chats")
                                    .updateChildren(lastMsgObj)
                                database!!.reference.child("chats")
                                    .child(receiverRoom!!)
                                    .updateChildren(lastMsgObj)
                                database!!.reference.child("chats")
                                    .child(senderRoom!!)
                                    .child("messages")
                                    .child(randomKey!!)
                                    .setValue(message).addOnSuccessListener {
                                        database!!.reference.child("chats")
                                            .child(receiverRoom!!)
                                            .child("messages")
                                            .child(randomKey)
                                            .setValue(message)
                                            .addOnSuccessListener {}
                                    }
                            }

                    }
            }
        }
}
*/

override fun onResume() {
    super.onResume()
    val currentId = FirebaseAuth.getInstance().uid
    mDbRef!!.reference.child("Presence")
        .child(currentId!!)
        .setValue("online")
}

override fun onPause() {
    super.onPause()
    val currentId = FirebaseAuth.getInstance().uid
    mDbRef!!.reference.child("Presence")
        .child(currentId!!)
        .setValue("offline")

}

}



