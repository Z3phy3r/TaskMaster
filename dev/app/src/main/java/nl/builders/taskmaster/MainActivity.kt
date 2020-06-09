package nl.builders.taskmaster

//import com.example.taksmasterapp.submissions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.taksmasterapp.Friends
import com.example.taksmasterapp.TaskQuestion
import com.example.taksmasterapp.TmTask
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference


class MainActivity : AppCompatActivity() {
    private var database: DatabaseReference? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("classinhoud", TaskQuestion().toString())
    }

    fun onTestButtonClicked(view : View){
        val intent = Intent(this, TmTask::class.java)
        startActivity(intent)
    }


}