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
        setContentView(R.layout.activity_main)
    }


}