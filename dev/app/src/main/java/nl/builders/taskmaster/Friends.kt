package com.example.taksmasterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import nl.builders.taskmaster.Friend
import nl.builders.taskmaster.R
import nl.builders.taskmaster.SubmissionScreenAdapter

class Friends : AppCompatActivity() {
    var friendList= arrayListOf<String>()
    var friendProfileList= arrayListOf<Friend>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.friends_screen)
        firebaseDataFriends()
    }
    private fun firebaseDataFriends() {
        friendList.clear()
        val queryRefFriends: Query? =
                FirebaseDatabase.getInstance().getReference("users").child(user)
                        .child("friendIDs")
        queryRefFriends?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                throw p0.toException()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (productSnapshot in p0.children) {
                    val friends = productSnapshot.getValue(String::class.java)
                    friendList.add(friends!!)
                }
                println(friendList)
                Log.e("firebase friends", friendList.toString())
                firebaseDataProfileFriends()
            }

        })
    }

    private fun firebaseDataProfileFriends() {
        friendList.forEach { t ->
            FirebaseDatabase.getInstance().getReference("users").child(t)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            throw p0.toException()
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            Log.e("log12", p0.toString())
                            val friend = p0.getValue(Friend::class.java)
                            friendProfileList.add(friend!!)
                            println(friendProfileList.toString())
                            Log.e("firebase friends profiles", friendProfileList.toString())
                        }
//                        val adapter = FriendScreenAdapter(this@Friends, friendProfileList)
//                        submissionsRecyclerView.adapter = adapter
//                        submissionsRecyclerView.layoutManager = LinearLayoutManager(this@Friends)
//                        val linearLayoutManager = LinearLayoutManager(this@Friends)
//                        linearLayoutManager.reverseLayout = true
//                        linearLayoutManager.stackFromEnd = true
//                        submissionsRecyclerView.setLayoutManager(linearLayoutManager)
//                        Log.e("firebase info friends", friendProfileList.toString())
                    })
        }
    }
}