package e.raguz.fitme

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import e.raguz.fitme.model.Exercise

class DoneAdapter( private val data: List<Exercise>, private val minutesMap: Map<String, Int>) : RecyclerView.Adapter<DoneAdapter.ContentViewHolder>() {

    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var name: TextView = itemView.findViewById(R.id.text_view_name)
        private var description : TextView = itemView.findViewById(R.id.text_view_description)
        private var minutes : TextView = itemView.findViewById(R.id.tv_minutes)

        fun bind(exercise : Exercise, min: Int){
            name.text = exercise.name
            description.text = Html.fromHtml(exercise.description).toString()
            minutes.text = "${min.toString()} min"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.done_exercise_adapter_item,
            parent,
            false
        )
        return DoneAdapter.ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = data[position]
        val minutes = minutesMap[item.id.toString()]

        holder.bind(item, minutes ?: 0)
    }

    override fun getItemCount(): Int = data.size
}