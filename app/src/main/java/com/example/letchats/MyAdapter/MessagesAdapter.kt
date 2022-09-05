package com.example.letchats.MyAdapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.letchats.R
import com.example.letchats.databinding.DeleteLayoutBinding
import com.example.letchats.databinding.ReceiverMsgBinding
import com.example.letchats.databinding.SenderMsgBinding
import com.example.letchats.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessagesAdapter(
    var context: Context,
    messagesList: ArrayList<Message>?,
    senderRoom: String,
    receiverRoom: String
   ) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private lateinit var messagesList: ArrayList<Message>
    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 2
    private var senderRoom: String
    var receiverRoom: String
    private lateinit var mDbRef: DatabaseReference



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            //inflate receive
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.receiver_msg, parent, false)
            ReceiveViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.sender_msg, parent, false)
            SentViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagesList[position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == currentMessage.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        if (holder.javaClass == SentViewHolder::class.java) {

            //do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            mDbRef = FirebaseDatabase.getInstance().reference
          viewHolder.binding.messageText.text = currentMessage.message

               viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                    val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener {
                  //  currentMessage.message = "message is removed"
                    currentMessage.messageId?.let { it1 ->
                        mDbRef.child("chats").child(senderRoom).child("messages")
                            .child(it1).setValue(null)
                    }
                   currentMessage.messageId.let { it1->
                       mDbRef.child("chats").child(receiverRoom).child("messages")
                                .child(it1!!).setValue(null)

                    }

                    dialog.dismiss()
                }
                binding.delete.setOnClickListener {
                    currentMessage.messageId.let {it1 ->
                       mDbRef.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancel.setOnClickListener { dialog.dismiss() }
                dialog.show()
                false
            }

        }
        else
        {

            //do stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.binding.messageText.text = currentMessage.message
/*
           viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.everyone.setOnClickListener {
                    currentMessage.message = ""
                    currentMessage.messageId?.let { it1 ->
                       mDbRef.child("chats").child(senderRoom).child("messages")
                            .child(it1).setValue(currentMessage)
                    }
                    currentMessage.messageId.let { it1->
                        mDbRef.child("chats").child(receiverRoom).child("messages")
                            .child(it1!!).setValue(currentMessage)

                    }

                    dialog.dismiss()
                }

                binding.delete.setOnClickListener {
                   currentMessage.messageId.let {it1 ->
                        mDbRef.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }

                binding.cancel.setOnClickListener { dialog.dismiss() }
                dialog.show()
                false
            }*/



        }

    }


    override fun getItemCount(): Int = messagesList!!.size



    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SenderMsgBinding = SenderMsgBinding.bind(itemView)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ReceiverMsgBinding = ReceiverMsgBinding.bind(itemView)
    }

    init {

        if(messagesList !=null){
            this.messagesList = messagesList
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }




}