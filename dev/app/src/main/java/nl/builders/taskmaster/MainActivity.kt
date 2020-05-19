package nl.builders.taskmaster

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


private var mStorageRef: StorageReference? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mStorageRef = FirebaseStorage.getInstance().reference;
        Log.e("hallo", "hallo")

Log.i("doei", "doei")
          
        Log.e("hallo", "doei")
    }
}
