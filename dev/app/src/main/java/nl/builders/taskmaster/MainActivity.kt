package nl.builders.taskmaster

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.taksmasterapp.submissions
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference

var setTasks= arrayListOf<TaskQuestion>()

data class TaskQuestion(
        val discription: String?="",
        val imagereff: String?="",
        val name: String?=""
)

class MainActivity : AppCompatActivity() {
    private var database: DatabaseReference? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseDatabase.getInstance()
        setContentView(R.layout.activity_main)
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
                        val tasks =productSnapshot.getValue((TaskQuestion()::class.java))
                        setTasks.add(tasks!!)
                        Log.e("setTasks", setTasks.toString())
                        Log.e("p0",productSnapshot.toString())
                    }
                }
            })

    }

}