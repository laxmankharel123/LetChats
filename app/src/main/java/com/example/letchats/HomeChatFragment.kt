package com.example.letchats


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letchats.MyAdapter.MyFriendListAdapter
import com.example.letchats.databinding.FragmentHomeChatBinding
import com.example.letchats.login.User
import com.google.firebase.firestore.*

class HomeChatFragment : Fragment() {

    private lateinit var userArrayList: ArrayList<User>
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyFriendListAdapter
    private lateinit var binding: FragmentHomeChatBinding
    private val database = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeChatBinding.inflate(inflater, container, false)

        context ?: binding.root

        recyclerView = binding.friendListRecyclerView

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // fetchData()
        getRecyclerView()

    }

    private fun fetchData() {
        database.collection("users")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    Log.d("TAG", "Document: $document")
                }
            }
    }


    private fun getRecyclerView() {

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.hasFixedSize()
        userArrayList = arrayListOf()
        myAdapter = MyFriendListAdapter(userArrayList)
        recyclerView.adapter = myAdapter
        EventChangeListener()
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userArrayList.add(dc.document.toObject(User::class.java))

                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }

            })

    }

}