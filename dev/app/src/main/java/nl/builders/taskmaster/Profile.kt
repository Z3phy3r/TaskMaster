package com.example.taksmasterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nl.builders.taskmaster.R

class Profile : AppCompatActivity() {
    var myTasks= arrayListOf<Tasks>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)
        firebaseDataUserProfile()
    }
    private fun firebaseDataUserProfile() {
        FirebaseDatabase.getInstance().getReference("submissions")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        throw p0.toException()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (productSnapshot in p0.children) {

                            // Log.e("user", it.child(user).toString())
                            val task = productSnapshot.child(user).getValue(Tasks::class.java)
                            myTasks.add(task!!)

                        }
                        println(myTasks)
                        Log.e("my task", myTasks.toString())
                    }
//                    val adapter = ProfileScreenAdapter(this@Profile, myTasks)
//                        submissionsRecyclerView.adapter = adapter
//                        submissionsRecyclerView.layoutManager = LinearLayoutManager(this@Profile)
//                        val linearLayoutManager = LinearLayoutManager(this@Profile)
//                        linearLayoutManager.reverseLayout = true
//                        linearLayoutManager.stackFromEnd = true
//                        submissionsRecyclerView.setLayoutManager(linearLayoutManager)
//                        Log.e("firebase info friends", friendProfileList.toString())
                })

    }
}