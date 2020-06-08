package nl.builders.taskmaster

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.taksmasterapp.submissions
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private var database: DatabaseReference? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseDatabase.getInstance()
        setContentView(R.layout.activity_main)
        var list:ArrayList<DataSnapshot> = ArrayList()
        database= FirebaseDatabase.getInstance().reference
        //database = FirebaseDatabase.getInstance().getReference("submissions/testTask1" )
        val childEventListener: ChildEventListener = FirebaseDatabase.getInstance().reference.child("submissions").child("testTask1")
            .addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               var index = 0
                for (testTask1: DataSnapshot in list){
                    list.add(testTask1)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })



    }

}