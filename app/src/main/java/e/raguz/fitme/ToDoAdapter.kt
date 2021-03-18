package e.raguz.fitme

import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import e.raguz.fitme.model.Exercise

class ToDoAdapter( private val data: List<Exercise>, val action : (id: Int, name: String, minutes : Int ) -> Unit,
    val actionDelete : (exercise:Exercise) -> Unit)
    : RecyclerView.Adapter<ToDoAdapter.ContentViewHolder>(){

    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var name: TextView = itemView.findViewById(R.id.text_view_name)
        private var description : TextView = itemView.findViewById(R.id.text_view_description)
        var input : TextInputLayout = itemView.findViewById(R.id.textField_input)
        var button : Button = itemView.findViewById(R.id.button_go)
        var close : ImageView = itemView.findViewById(R.id.iv_close)

        init {
            input.editText?.doAfterTextChanged {
                if (it.toString().isNotEmpty())
                {
                    val min = it.toString().toInt()
                    if (min <= 0){
                        input.editText?.setText(R.string.default_value)
                    }
                    button.isEnabled = true
                }
              else{
                  button.isEnabled = false
                }
            }
        }
        fun bind(exercise : Exercise){
            name.text = exercise.name
            description.text = Html.fromHtml(exercise.description).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.todo_exercise_adapter_item,
            parent,
            false
        )

        return ToDoAdapter.ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

        holder.button.setOnClickListener {
            action(item.id, item.name, holder.input.editText?.text.toString().toInt() )
        }
        holder.close.setOnClickListener {
            actionDelete(item)
        }
    }

    override fun getItemCount(): Int = data.size

}