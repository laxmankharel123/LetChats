package com.example.letchats

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.letchats.MyAdapter.MessagesAdapter
import com.example.letchats.MyAdapter.bindImageForSVG
import com.example.letchats.databinding.ActivityChatPersonBinding
import com.example.letchats.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_chat_person.*
import kotlinx.android.synthetic.main.activity_chat_person.view.*
import kotlinx.android.synthetic.main.item_friend_list.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatPersonActivity : AppCompatActivity() {

    var binding: ActivityChatPersonBinding? = null
    var adapter: MessagesAdapter? = null
    var messages: ArrayList<Message>? = null
    var senderRoom: String? = null
    var receiverRoom: String? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var dialog: ProgressDialog? =null
    var senderUid: String? =null
    var receiverUid: String? =null


    // var fireBaseUser: FirebaseUser? = null
    // var reference: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatPersonBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
     //   dialog = ProgressDialog(this@ChatPersonActivity)
     //   dialog!!.setMessage("uploading image...")
        messages = ArrayList()
        val userName = intent.getStringExtra("userid")
        val photo = intent.getStringExtra("photo")
        //var email = intent.getStringExtra("email")
        Glide.with(this)
            .load(photo)
            .placeholder(R.drawable.ic_chat_logo)
            .transform(CircleCrop())
            .into(binding!!.profileImage)

        // bindImageForSVG(profile_image, photo)
        profile_name.text = userName

        imageView.setOnClickListener {
            finish()
        }

        receiverUid = intent.getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().uid
        Log.d("TAG", "senderUid $senderUid")
        database!!.reference.child("Presence").child(receiverUid!!)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val status = snapshot.getValue(String::class.java)
                        if(status == "offline")
                        {
                           binding!!.status.visibility = View.GONE

                        }
                        else
                        {
                            binding!!.status.setText(status)
                            binding!!.status.visibility = View.VISIBLE

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid +senderUid
        adapter = MessagesAdapter(this@ChatPersonActivity, messages, senderRoom!!, receiverRoom!!)
        binding!!.recyclerChat.layoutManager = LinearLayoutManager(this@ChatPersonActivity)
        binding!!.recyclerChat.adapter = adapter
        database!!.reference.child("chats")
            .child(senderRoom!!)
            .child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages!!.clear()
                    for (snapshot1 in snapshot.children)
                    {
                        val message: Message? = snapshot1.getValue(Message::class.java)
                        message!!.messageId = snapshot1.key
                        messages!!.add(message)

                    }

                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        binding!!.buttonSend.setOnClickListener {
            val messageTxt: String = binding!!.editMessage.text.toString()
            val date = Date()
            val message = Message(messageTxt, senderUid, date.time)


            binding!!.editMessage.setText("")
            val randomKey = database!!.reference.push().key
            val lastMsgObj = HashMap<String, Any>()
            lastMsgObj["lastMsg"] = message.message!!
            lastMsgObj["lastMsgTime"] = date.time


            database!!.reference.child("chats").child(senderRoom!!)
                .updateChildren(lastMsgObj)
            database!!.reference.child("chats").child(receiverRoom!!)
                .updateChildren(lastMsgObj)
            database!!.reference.child("chats").child(senderRoom!!)
                .child("message")
                .child(randomKey!!)
                .setValue(message).addOnSuccessListener {
                    database!!.reference.child("chats")
                        .child(receiverRoom!!)
                        .child("message")
                        .child(randomKey)
                        .setValue(message)
                        .addOnSuccessListener {

                        }
                }

        }
        val handler = Handler()
        binding!!.editMessage.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                database!!.reference.child("Presence")
                    .child(senderUid!!)
                    .setValue("typing.....")

                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userStoppedTyping, 1000)
            }

            var userStoppedTyping = Runnable {
                database!!.reference.child("Presence")
                    .child(senderUid!!)
                    .setValue("Online")

            }

        })
        supportActionBar!!.setDisplayShowTitleEnabled(false)





        // fireBaseUser = FirebaseAuth.getInstance().currentUser
        // reference = FirebaseFirestore.getInstance().collection("users")


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 25)
            if(data!=null){
                if(data.data!=null)
                {
                    val selecctedImage = data.data
                    val calendar = Calendar.getInstance()
                    val refrence = storage!!.reference.child("chats")
                        .child(calendar.timeInMillis.toString()+"")
                    dialog!!.show()
                    refrence.putFile(selecctedImage!!)
                        .addOnCompleteListener { task->
                            dialog!!.dismiss()
                            if(task.isSuccessful)
                                refrence!!.downloadUrl.addOnSuccessListener { uri->
                                    val filePath = uri.toString()
                                    val messageTxt : String = binding!!.editMessage.text.toString()
                                    val date = Date()
                                    val message = Message(messageTxt, senderUid, date.time)
                                    message.message = "photo"
                                    message.imageUrl= filePath
                                    binding!!.editMessage.setText("")
                                    val randomKey = database!!.reference.push()
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
                                        .child(randomKey.toString())
                                        .setValue(message).addOnSuccessListener {
                                            database!!.reference.child("chats")
                                                .child(receiverRoom!!)
                                                .child("messages")
                                                .child(randomKey.toString())
                                                .setValue(message)
                                                .addOnSuccessListener{}
                                        }
                                }

                        }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("offline")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("offline")

    }
}



