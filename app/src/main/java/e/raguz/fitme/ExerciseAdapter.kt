package e.raguz.fitme

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import e.raguz.fitme.model.Exercise
import android.text.Html
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

class ExerciseAdapter(val action : (exercise: Exercise) -> Unit) : RecyclerView.Adapter<ExerciseAdapter.ContentViewHolder>() {

    private var data: List<Exercise> = listOf()
    private var addedExercises : List<Exercise> = listOf()

    fun setData(data: List<Exercise>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setAddedExercises(data: List<Exercise>){
        this.addedExercises = data
        notifyDataSetChanged()
    }

    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var name: TextView = itemView.findViewById(R.id.text_view_name)
        private var description : TextView = itemView.findViewById(R.id.text_view_description)
        private var added :ImageView = itemView.findViewById(R.id.iv_Added)

        fun bind(exercise : Exercise, isAdded: Boolean){
            name.text = exercise.name
            description.text = Html.fromHtml(exercise.description).toString()
            if (isAdded){

                added.setColorFilter(ContextCompat.getColor(added.context, R.color.colorPrimary))
            }
            else{
                added.setColorFilter(ContextCompat.getColor(added.context, R.color.simple_gray))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.exercise_adapter_item,
            parent,
            false
        )

        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = data[position]
        val isAdded = addedExercises.firstOrNull { it.id == item.id }
        holder.bind(item, isAdded != null)

        holder.itemView.setOnClickListener {
            action(item)
        }
    }

    override fun getItemCount(): Int = data.size

}