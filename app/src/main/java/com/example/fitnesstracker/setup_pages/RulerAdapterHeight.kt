import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstracker.R

class RulerAdapterHeight(private val heights: List<Int>) :
    RecyclerView.Adapter<RulerAdapterHeight.RulerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ruler_tick, parent, false)
        return RulerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RulerViewHolder, position: Int) {
        holder.tickText.text = heights[position].toString()
    }

    override fun getItemCount(): Int = heights.size

    class RulerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tickText: TextView = view.findViewById(R.id.tickText)
    }
}