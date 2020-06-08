package nl.builders.taskmaster

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taksmasterapp.TaskQuestion
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.task_screen_adapter.view.*

data class taskScreenAdapter(
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

        fileRef = FirebaseStorage.getInstance().reference.child("testTask1").child(taskList[position].imagereff!!)

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
        val curTaskName = taskList[position].name        //  taskList[position].storageID // images[position]  // +1 per swipe position = INT
        val curDescription = taskList[position].discription//titles[position]
        holder.itemView.descriptionTextView.setText(curDescription)
        holder.itemView.titleTextView.setText(curTaskName)

    }
}
