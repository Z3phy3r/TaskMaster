package nl.builders.testdatabase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.getInstance
import android.annotation.SuppressLint as SuppressLint1
import android.widget.Button as Button1
import com.google.firebase.database.DataSnapshot as DataSnapshot1

private var mDatabase: DatabaseReference? = null

var sortOnFriends:Boolean=false
var sortOnRating:Boolean=true
var userUID="f2IX4FQb4NRlmxPfwkmJlYo7FNC2"
var user: String= "f2IX4FQb4NRlmxPfwkmJlYo7FNC2"
var friendList= arrayListOf<String>()
var friendProfileList= arrayListOf<Friend>()
var myTasks= arrayListOf<Task>()
var currentRating= arrayListOf<Rating>()
var setTasks= arrayListOf<TaskQuestion>()
var task="testTask1"
data class Friend(
    val userID: String?="",
    val profilePic: String?=""
)
data class TaskQuestion(
    val discription: String?="",
    val imagereff: String?="",
    val name: String?=""
)
data class Task(
    val ranking: Double?=-1.0,
    val storageID:String?="",
    val description:String?="",
    val userID:String?="",
    val userUID:String?=""// get from tasklist when clicked on in listview
)
data class Rating(
    val nrVotes: Double?=-1.0,
    val sumOfVotes: Double?=-1.0
)
fun ranking(tasklist: Task): Double? = tasklist.ranking

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button1
    lateinit var ratingBar: RatingBar
    var ratingTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById<View>(R.id.button) as Button1
        ratingBar = findViewById<View>(R.id.ratingBar) as RatingBar
        ratingTextView = findViewById(R.id.RatingTextView)
        mDatabase = getInstance().getReference("submissions").child(task)
        ratingBar.rating = 3.0F// get ranking from tasklist when clicked on in listview
        Log.e("friendlist", friendList.toString())
        firebaseDataFriends()
        getTasks()

    }

                            private fun getTasks() {
        getInstance().getReference("tasks")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot1) {
                    for (productSnapshot in p0.children) {
                       val tasks =productSnapshot.getValue((TaskQuestion()::class.java))
                        setTasks.add(tasks!!)
                        Log.e("setTasks", setTasks.toString())
                       Log.e("p0",productSnapshot.toString())
                    }
                }
            })
    }

                            private fun firebaseDataFriends() {
                        friendList.clear()
                        val queryRefFriends: Query? =
                            getInstance().getReference("users").child(user)
                                .child("friendIDs")
                        queryRefFriends?.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                throw p0.toException()
                            }

                            override fun onDataChange(p0: DataSnapshot1) {
                                for (productSnapshot in p0.children) {
                                    val friends = productSnapshot.getValue(String::class.java)
                                    friendList.add(friends!!)
                                }
                                println(friendList)
                                Log.e("firebase friends", friendList.toString())
                                firebaseDataRetriever()
                                firebaseDataProfileFriends()
                                firebaseDataUserProfile()
                            }

                        })
                    }

                            private fun firebaseDataProfileFriends() {
                        friendList.forEach { t ->
                            getInstance().getReference("users").child(t)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        throw p0.toException()
                                    }

                                    override fun onDataChange(p0: DataSnapshot1) {
                                        Log.e("log12", p0.toString())
                                        val friend = p0.getValue(Friend::class.java)
                                        friendProfileList.add(friend!!)
                                        println(friendProfileList.toString())
                                        Log.e("firebase friends profiles", friendProfileList.toString())
                                    }

                                })
                        }
                    }

                            private fun firebaseDataUserProfile() {
                        getInstance().getReference("submissions")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    throw p0.toException()
                                }

                                override fun onDataChange(p0: DataSnapshot1) {
                                    for (productSnapshot in p0.children) {

                                        // Log.e("user", it.child(user).toString())
                                        val task = productSnapshot.child(user).getValue(Task::class.java)
                                        myTasks.add(task!!)

                                    }
                                    println(myTasks)
                                    Log.e("my task", myTasks.toString())
                                }
                            })

                    }

                            private fun firebaseDataRetriever() {
                        val taskList: ArrayList<Task> = ArrayList()
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

                                    override fun onDataChange(p0: DataSnapshot1) {
                                        Log.e("hoi", p0.toString())
                                        val task = p0.getValue(Task::class.java)
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

                                override fun onDataChange(p0: DataSnapshot1) {
                                    for (productSnapshot in p0.children) {
                                        val task = productSnapshot.getValue(Task::class.java)
                                        taskList.add(task!!)
                                    }

                                    println(taskList)
                                    Log.e("firebase info", taskList.toString())
                                }
                            })
                        }
                    }

                            fun click(view: View) {
                            getInstance().getReference("submissions").child(task).child(userUID)
                                .child("rating").child("voted")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        throw p0.toException()
                                    }

                                    override fun onDataChange(p0: com.google.firebase.database.DataSnapshot) {
                                        Log.e("snapshot", p0.value.toString())
                                        if (p0.value.toString().contains(user)) {
                                            Toast.makeText(applicationContext , "already voted!!", Toast.LENGTH_LONG).show()
                                        } else {
                                            getInstance().getReference("submissions").child(task).child(userUID)
                                                .child("rating")
                                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onCancelled(p0: DatabaseError) {
                                                        throw p0.toException()
                                                    }

                                                    @SuppressLint1("SetTextI18n")
                                                    override fun onDataChange(p0: DataSnapshot1) {
                                                        val rating = p0.getValue(Rating::class.java)
                                                        currentRating.add(rating!!)
                                                        println(currentRating)
                                                        val ratingValue: Float = ratingBar.rating
                                                        ratingTextView?.text = "The Rating is: $ratingValue"
                                                        val oldSumOfVotes = currentRating[0].sumOfVotes
                                                        val oldNrVotes: Double? = currentRating[0].nrVotes
                                                        val nrVotes: Double? = 1.0 + oldNrVotes!!
                                                        val sumOfVotes: Double? = oldSumOfVotes?.plus(ratingValue)
                                                        val ranking: Double? = sumOfVotes?.div(nrVotes!!)
                                                        getInstance().getReference("submissions").child(task)
                                                            .child(userUID)
                                                            .child("rating").child("nrVotes").setValue(nrVotes)
                                                        getInstance().getReference("submissions").child(task)
                                                            .child(userUID)
                                                            .child("rating").child("sumOfVotes").setValue(sumOfVotes)
                                                        getInstance().getReference("submissions").child(task)
                                                            .child(userUID)
                                                            .child("ranking").setValue(ranking)
                                                        getInstance().getReference("submissions").child(task)
                                                            .child(userUID)
                                                            .child("rating").child("voted").child(user).setValue(1)
                                                    }
                                                })
                                        }
                                    }


                                })


                        }
}



