package com.example.taksmasterapp

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taksmasterapp.TaskQuestion
import com.example.taksmasterapp.UploadTask
import com.example.taksmasterapp.submissions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.task_screen_adapter.view.*


var nextScreenTask: String? = ""
var nextScreenDesc: String? = ""

data class taskScreenAdapter(
    var mContext:Context,
    val taskList: List<TaskQuestion> // in de list kan je een class stoppen
) : RecyclerView.Adapter<taskScreenAdapter.ViewPagerViewHolder> ()
{
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_screen_adapter, parent, false)
        return ViewPagerViewHolder(view)


    }

    override fun getItemCount(): Int {
        return taskList.size

    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        var bmp : Bitmap? = null
        var fileRef: StorageReference? = null

        Log.d("tasklistposition" , taskList[position].imagereff!!.toString() )
        fileRef = FirebaseStorage.getInstance().reference.child("taskImages").child(taskList[position].imagereff!!)


        Log.e("fileref", fileRef.toString())

        if (fileRef != null) {

            val ONE_MEGABYTE = (1920 * 1080).toLong()
            fileRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    println(bmp)
                    holder.itemView.taskImageView.setImageBitmap(bmp)

                }
                .addOnFailureListener { exception ->
                    println("fail")
                }
        } else {

        }
        var curTaskName = taskList[position].name        //  taskList[position].storageID // images[position]  // +1 per swipe position = INT
        var curDescription = taskList[position].discription //titles[position]
        holder.itemView.descriptionTextView.setText(curDescription)
        holder.itemView.titleTextView.setText(curTaskName)
        holder.itemView.buttonViewSubmissions.setOnClickListener({v -> viewSub() })
        holder.itemView.buttonSubmit.setOnClickListener({v -> submit() })
       nextScreenTask = curTaskName
       nextScreenDesc = curDescription

    }



    private fun viewSub() {

        val intent = Intent(mContext, submissions::class.java).apply{
              this.putExtra("currentTask", nextScreenTask)
              this.putExtra("currentDescription", nextScreenDesc)


        }
       mContext.startActivity(intent)
    }
    private fun submit() {
        FirebaseDatabase.getInstance().getReference("submissions").child(task)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        throw p0.toException()
                    }

                    override fun onDataChange(p0: com.google.firebase.database.DataSnapshot) {
                        Log.e("userUID", userUID.toString())
                        Log.e("snapshot", p0.value.toString())
                        if (p0.value.toString().contains(userUID)) {
                            Toast.makeText(mContext, "already sumbmited!!", Toast.LENGTH_LONG).show()
                        } else {

                            val intent = Intent(mContext, UploadTask::class.java).apply {
                                this.putExtra("currentTask", nextScreenTask)
                                this.putExtra("currentDescription", nextScreenDesc)
                            }
                            mContext.startActivity(intent)
                        }

                    }
                })
    }
}



