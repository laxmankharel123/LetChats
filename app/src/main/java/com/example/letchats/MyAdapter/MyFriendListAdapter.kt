package com.example.letchats.MyAdapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.letchats.ChatPersonActivity
import com.example.letchats.Preferences
import com.example.letchats.R
import com.example.letchats.login.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


open class MyFriendListAdapter(private val userList: ArrayList<User>) :
    RecyclerView.Adapter<MyFriendListAdapter.MyViewHolder>() {
    private lateinit var mAuth: FirebaseAuth
   // private lateinit var prefernce: Preferences


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_friend_list, parent, false)
        return MyViewHolder(itemView)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: MyFriendListAdapter.MyViewHolder, position: Int) {

        val user = userList[position]
        holder.bind(userList[position])



        /* val user: User = userList[position]
         holder.myName.text = user.name
         holder.email.text = user.email
         Glide.with().load(user.photo).into(holder.image)*/

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatPersonActivity::class.java)
            intent.putExtra("userid", user.name)
            intent.putExtra("photo", user.photo)
            intent.putExtra("email", user.email)
            intent.putExtra("uid", user.uid)
            holder.itemView.context.startActivity(intent)

        }
    }


    // return the number of the items in the list
    override fun getItemCount() = userList.size

    // Holds the views for adding it to image and text

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myName: TextView = itemView.findViewById(R.id.friend_name)
        private val email: TextView = itemView.findViewById(R.id.details)
        private val image: ImageView = itemView.findViewById(R.id.image_profile)

        fun bind(user: User) {
            mAuth = FirebaseAuth.getInstance()
            if ((user.email!! != mAuth.currentUser!!.email) && user.photo != null) {
                myName.text = user.name
                email.text = user.email
                bindMenuImageFromUrl(
                    image as AppCompatImageView,
                    user.photo
                )

            }
        }


    }

}



