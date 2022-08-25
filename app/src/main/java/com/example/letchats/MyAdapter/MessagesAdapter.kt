package com.example.letchats.MyAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letchats.R
import com.example.letchats.databinding.SenderMsgBinding
import com.example.letchats.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessagesAdapter(
    var context: Context,
    messages: ArrayList<Message>?,
    senderRoom: String,
    receiverRoom: String
): RecyclerView.Adapter<RecyclerView.ViewHolder?>()
{
    lateinit var messages: ArrayList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    var senderRoom:String
    var receiverRoom: String


    inner class  SentMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var binding: SenderMsgBinding = SenderMsgBinding.bind(itemView)
    }

    inner class ReceiveMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var binding: SenderMsgBinding = SenderMsgBinding.bind(itemView)
    }

    init {
        if(messages != null){
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT){
            val view: View = LayoutInflater.from(context).inflate(R.layout.sender_msg, parent, false)
            SentMsgHolder(view)
        }
        else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.receiver_msg, parent, false)
            ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
      val messages = messages[position]
        return if(FirebaseAuth.getInstance().uid == messages.senderId)
        {
            ITEM_SENT
        }
        else{
            ITEM_RECEIVE
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder.javaClass == SentMsgHolder::class.java){
            val viewHolder = holder as SentMsgHolder
            if(message.message.equals("photo")){
                viewHolder.binding.image.visibility =  View.VISIBLE
                viewHolder.binding.messageText.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.ic_chat_logo)
                        .into(viewHolder.binding.image)

            }
            else
            {
                val viewHolder = holder as ReceiveMsgHolder
                if(message.message.equals("photo")){
                    viewHolder.binding.image.visibility =  View.VISIBLE
                    viewHolder.binding.messageText.visibility = View.GONE
                    viewHolder.binding.mLinear.visibility = View.GONE
                    Glide.with(context)
                        .load(message.imageUrl)
                        .placeholder(R.drawable.ic_chat_logo)
                        .into(viewHolder.binding.image)

                }
            }

           // viewHolder.binding.messageText.text = message.message
         //   viewHolder.itemView.setOnLongClickListener {
          //      val view = LayoutInflater.from(context).inflate(R.layout.de)
        //    }
        }

    }

    override fun getItemCount(): Int = messages.size


}