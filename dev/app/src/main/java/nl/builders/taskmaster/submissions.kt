package com.example.taksmasterapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import nl.builders.taskmaster.R

var sortOnFriends:Boolean=false
var sortOnRating:Boolean=true
var friendList= arrayListOf<String>()

data class Tasks(
        val ranking: Double?=-1.0,
        val storageID:String?="",
        val description:String?="",
        val userID:String?="",
        val userUID:String?=""// get from tasklist when clicked on in listview
)
fun ranking(tasklist: Tasks): Double? = tasklist.ranking

class submissions : AppCompatActivity() {
    private var mDatabase: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.submissions_screen)
        mDatabase = FirebaseDatabase.getInstance().getReference("submissions").child(task)
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
                firebaseDataRetriever()
            }

        })
    }

    private fun firebaseDataRetriever() {
        val taskList: ArrayList<Tasks> = ArrayList()
        //layoutManager.setReverseLayout(true) om de volg horde om te draaien
        //layoutManager.setStackFromEnd(true)
        if (sortOnFriends) {
            Log.e("firebaseDataSorting", "sort on friends")
            taskList.clear()
            friendList.forEach { s ->
                println("foreach$s")
                mDatabase?.child(s)?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        throw p0.toException()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.e("hoi", p0.toString())
                        val task = p0.getValue(Tasks::class.java)
                        taskList.add(task!!)
                        Log.e("taskList", taskList.toString())
                        if (sortOnRating) {
                            taskList.sortByDescending { ranking(it) }
                            Log.e("taskList2", taskList.toString())
                        }
                    }
                })
            }


        } else {
            val queryRef: Query? = if (sortOnRating) {
                mDatabase?.orderByChild("/ranking")?.limitToLast(10)
            } else {
                mDatabase?.limitToFirst(10)
            }
            queryRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (productSnapshot in p0.children) {
                        val task = p0.child(user).getValue(Tasks::class.java)
                        taskList.add(task!!)
                    }

                    println(taskList)
                    Log.e("firebase info", taskList.toString())
                }
            })
        }
    }

}