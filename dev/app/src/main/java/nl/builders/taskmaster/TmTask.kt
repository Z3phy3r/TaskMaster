package com.example.taksmasterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.taksmasterapp.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.task_screen.*
import com.example.taksmasterapp.taskScreenAdapter

var setTasks= arrayListOf<TaskQuestion>()
var uid: String? = null;

class TmTask : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_screen)
        mDatabase = FirebaseDatabase.getInstance().getReference("tasks")
        val intent: Intent = getIntent()
        uid = intent.getStringExtra("uid")
        getTasks()
    }
    private fun getTasks() {
        FirebaseDatabase.getInstance().getReference("tasks")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (productSnapshot in p0.children) {
                        val tasks =productSnapshot.getValue((TaskQuestion()::class.java)
                        )
                        setTasks.add(tasks!!)
                        Log.e("setTasks", setTasks.toString())
                        Log.e("p0",productSnapshot.toString())
                    }
                    val adapter = taskScreenAdapter(this@TmTask,setTasks)
                    viewPager.adapter = adapter

                }
            })

    }

}