package nl.builders.testdatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view_pager.view.*

class ViewPagerAdapter (
    val images: List<Int> // in de list kan je een class stoppen
    ) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> ()
    {
        inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
            return ViewPagerViewHolder(view)
        }

        override fun getItemCount(): Int {
            return images.size //list. size
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
            val curImage = images[position]  // +1 per swipe position = INT
            holder.itemView.imageView2.setImageResource(curImage)

        }
    }

// op rating ,time added, user