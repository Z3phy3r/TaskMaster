package com.example.taksmasterapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.view_task_rating_screen.*


class Rating : AppCompatActivity() {
    var ratingTextView: TextView? = null
    var rateBar: RatingBar?=null
    var imageReff="null"
    var ratingFirebase: Float?=null
    var userUIDpost="null"
    var currentRating= arrayListOf<RatingData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_task_rating_screen)
        ratingTextView = findViewById(R.id.textView)
        rateBar=findViewById(R.id.ratingBar)
        var imageView: ImageView?=null
        val intent: Intent = getIntent()



        var user="f2IX4FQb4NRlmxPfwkmJlYo7FNC2"  //add from last page taskList.index.userUID
        userUIDpost =intent.getStringExtra("currentUser")
        Log.e("userUIDPost",userUIDpost)
        ratingFirebase =intent.getFloatExtra("currentRanking",0F)
        Log.e("ratingfirebase",ratingFirebase.toString())
        var task = "testTask1"//intent.getStringExtra("currentTask")ta1@gmi
        imageReff =intent.getStringExtra("currentImageReff")
        Log.e("imagereff",imageReff)
        rateBar?.rating=ratingFirebase!!.toFloat()
    getFile()
        getDiscription()

    }

fun getDiscription() {
    FirebaseDatabase.getInstance().getReference("submissions").child(task).child(userUIDpost).child("description")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var discription= p0.getValue(String::class.java)
                    Log.e("discription",p0.toString())
                    Log.e("discription",discription.toString())
                    textView.setText(discription)

                }
            })
}


    fun rate(view: View) {
        FirebaseDatabase.getInstance().getReference("submissions").child(task).child(userUIDpost)
                .child("rating").child("voted")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        throw p0.toException()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.e("snapshot", p0.value.toString())
                        if (p0.value.toString().contains(user)) {
                            Toast.makeText(applicationContext , "already voted!!", Toast.LENGTH_LONG).show()
                        } else {
                            FirebaseDatabase.getInstance().getReference("submissions").child(task).child(userUIDpost)
                                    .child("rating")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                            throw p0.toException()
                                        }

                                        @SuppressLint("SetTextI18n")
                                        override fun onDataChange(p0: DataSnapshot) {
                                            val rating = p0.getValue(RatingData::class.java)
                                            currentRating.add(rating!!)
                                            println(currentRating)
                                            val ratingValue: Float = ratingBar!!.rating
                                            ratingTextView?.text = "The Rating is: $ratingValue"
                                            val oldSumOfVotes = currentRating[0].sumOfVotes
                                            val oldNrVotes: Double? = currentRating[0].nrVotes
                                            val nrVotes: Double? = 1.0 + oldNrVotes!!
                                            val sumOfVotes: Double? = oldSumOfVotes?.plus(ratingValue)
                                            val ranking: Double? = sumOfVotes?.div(nrVotes!!)
                                            FirebaseDatabase.getInstance().getReference("submissions").child(task)
                                                    .child(userUIDpost)
                                                    .child("rating").child("nrVotes").setValue(nrVotes)
                                            FirebaseDatabase.getInstance().getReference("submissions").child(task)
                                                    .child(userUIDpost)
                                                    .child("rating").child("sumOfVotes").setValue(sumOfVotes)
                                            FirebaseDatabase.getInstance().getReference("submissions").child(task)
                                                    .child(userUIDpost)
                                                    .child("ranking").setValue(ranking)
                                            FirebaseDatabase.getInstance().getReference("submissions").child(task)
                                                    .child(userUIDpost)
                                                    .child("rating").child("voted").child(user).setValue(1)
                                        }
                                    })
                        }
                    }


                })


    }
    fun getFile(){
        var bmp : Bitmap? = null
        var fileRef: StorageReference? = null
        fileRef = FirebaseStorage.getInstance().reference.child(task).child(imageReff)

        if (fileRef != null) {

            val ONE_MEGABYTE = (1920 * 1080).toLong()
            fileRef.getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener { bytes ->
                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        println(bmp)
                        imageView?.setImageBitmap(bmp)

                    }
                    .addOnFailureListener { exception ->
                        println("fail")
                    }
        } else {

        }
    }
}