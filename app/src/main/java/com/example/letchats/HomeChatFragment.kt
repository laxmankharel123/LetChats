package com.example.letchats


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letchats.MyAdapter.MessagesAdapter
import com.example.letchats.MyAdapter.MyFriendListAdapter
import com.example.letchats.databinding.FragmentHomeChatBinding
import com.example.letchats.login.User
import com.example.letchats.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeChatFragment : Fragment() {

    lateinit var userList: ArrayList<User>
    var binding: FragmentHomeChatBinding? = null
    private lateinit var adapter: MyFriendListAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var mDbRef: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeChatBinding.inflate(inflater, container, false)
        context ?: binding!!.root
        recyclerView = binding!!.friendListRecyclerView
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView()

    }


    private fun getRecyclerView() {
        mDbRef = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        userList = ArrayList()
        adapter = MyFriendListAdapter(this@HomeChatFragment, userList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        mDbRef.reference
            .child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(User::class.java)
                        if ( mAuth.currentUser!!.uid != currentUser!!.userId) {

                            userList.add(currentUser)
                        }

                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
    }






        /*val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding!!.friendListRecyclerView.layoutManager = layoutManager
        // recyclerView.hasFixedSize()

        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList = snapshot.getValue(User::class.java)


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })*/
        /*binding!!.friendListRecyclerView.adapter = adapter
        database!!.reference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users!!.clear()
                for (snapshot1 in snapshot.children) {
                    val user: User? = snapshot1.getValue(User::class.java)
                    if (!user!!.userId.equals(FirebaseAuth.getInstance().uid))
                        if (user.email != mAuth.currentUser!!.email) {
                            users!!.add(user)
                        }

                }

                usersAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/
        //EventChangeListener()


/*    private fun EventChangeListener() {
       // db = FirebaseFirestore.getInstance()
      //  mAuth = FirebaseAuth.getInstance()


        binding.
        db.collection("users").orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val users = dc.document.toObject(User::class.java)
                            if (users.email != mAuth.currentUser!!.email) {
                                users.add(dc.document.toObject(User::class.java))
                            }
                        }


                    }

                    myAdapter.notifyDataSetChanged()
                }

            })

    }*/

/*
    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        mDbRef!!.reference.child("presence")
            .child(currentId!!).setValue("Online")

    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("Offline")

    }
*/

}