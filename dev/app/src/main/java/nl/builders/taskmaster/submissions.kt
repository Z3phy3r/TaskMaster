package com.example.taksmasterapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.submissions_screen.*
import com.example.taksmasterapp.R
import com.example.taksmasterapp.SubmissionScreenAdapter
import com.example.taksmasterapp.Tasks

var user: String = "f2IX4FQb4NRlmxPfwkmJlYo7FNC2"
var task: String = "testTask1"
var description: String? = ""
var sortOnFriends:Boolean=false
var sortOnRating:Boolean=true
var friendList= arrayListOf<String>()

// var setUserTasks= arrayListOf<Tasks>()

fun ranking(tasklist: Tasks): Double? = tasklist.ranking

class submissions : AppCompatActivity() {
    private var mDatabase: DatabaseReference? = null
  override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
     setContentView(R.layout.submissions_screen)

      mDatabase = FirebaseDatabase.getInstance().getReference("submissions").child(task)
//      task = intent.getStringExtra("currentTask")
//      description = intent.getStringExtra("currentDescription")


      Log.e("chef", task)

      firebaseDataFriends()
    }

    fun worldButtonClicked(view: View){
        sortOnFriends = !sortOnFriends
        firebaseDataFriends()
    }
    fun ratingButtonClicked(view: View){
        sortOnRating = !sortOnRating
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
                    println(friendList)
                    Log.e("firebase friends", friendList.toString())
                    firebaseDataRetriever()
                }

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
            mDatabase?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }
                override fun onDataChange(p0: DataSnapshot) {
                    Log.e("firebase friends", friendList.toString())
                    friendList.forEach { s ->
                        val task = p0.child(s).getValue(Tasks::class.java)
                        if(task!=null){
                            taskList.add(task)
                            Log.e("taskListhoi", taskList.toString())
                        }
                        if (sortOnRating) {
                            taskList.sortByDescending { ranking(it) }
                            Log.e("taskList2", taskList.toString())
                        }
                    }

                    val adapter = SubmissionScreenAdapter(this@submissions, taskList)
                    submissionsRecyclerView.adapter = adapter
                    submissionsRecyclerView.layoutManager = LinearLayoutManager(this@submissions)
                    val linearLayoutManager = LinearLayoutManager(this@submissions)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    submissionsRecyclerView.setLayoutManager(linearLayoutManager)
                    println(taskList)
                    Log.e("firebase info", taskList.toString())
                }
            })

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
                        val task = productSnapshot.getValue(Tasks::class.java)
                        taskList.add(task!!)


                    }

                  val adapter = SubmissionScreenAdapter(this@submissions, taskList)
                 submissionsRecyclerView.adapter = adapter
                    submissionsRecyclerView.layoutManager = LinearLayoutManager(this@submissions)
                    val linearLayoutManager = LinearLayoutManager(this@submissions)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    submissionsRecyclerView.setLayoutManager(linearLayoutManager)
                    println(taskList)
                    Log.e("firebase info", taskList.toString())
                }
            })
        }
    }

}