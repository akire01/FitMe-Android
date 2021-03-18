package e.raguz.fitme

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import e.raguz.fitme.database.ExercisesDatabase
import e.raguz.fitme.databinding.FragmentToDoBinding
import e.raguz.fitme.model.Exercise
import e.raguz.fitme.model.ExerciseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ToDoFragment : Fragment(R.layout.fragment_to_do) {

    private lateinit var exerciseDao: ExerciseDao
    private lateinit var binding : FragmentToDoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseDao = ExercisesDatabase.getDatabase(requireActivity().application).exerciseDao()
        binding = FragmentToDoBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = getString(R.string.title_my_exercises)
        binding.toDoRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        //navigateToTimer("Test", 1)
        refresh()

    }

    private fun refresh() {
        GlobalScope.launch(Dispatchers.IO){
            val data = exerciseDao.exercises

            val pref: SharedPreferences = requireContext().applicationContext
                .getSharedPreferences(getString(R.string.myPref), 0)

            val todo = data.filter { !pref.contains(it.id.toString()) }

            withContext(Dispatchers.Main){
                binding.toDoRecyclerView.adapter = ToDoAdapter(
                    todo,
                    { id, name, minutes ->
                        navigateToTimer(id, name, minutes)
                    }, { exercise ->
                        deleteExercise(exercise)
                    })
            }

        }
    }
    private fun deleteExercise(exercise: Exercise) {
       GlobalScope.launch(Dispatchers.IO){
           exerciseDao.deleteExercise(exercise)
           refresh()
       }

        Toast.makeText(
            requireContext(), "You removed ${exercise.name} from your ToDo list",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun navigateToTimer(id: Int, name: String, minutes: Int) {
        val action = ToDoFragmentDirections.actionGlobalTimerFragment(minutes, name, id)
        findNavController().navigate(action)
    }
}