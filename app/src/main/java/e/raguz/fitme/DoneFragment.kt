package e.raguz.fitme

import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import e.raguz.fitme.database.ExercisesDatabase
import e.raguz.fitme.databinding.FragmentDoneBinding
import e.raguz.fitme.databinding.FragmentToDoBinding
import e.raguz.fitme.model.Exercise
import e.raguz.fitme.model.ExerciseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt


class DoneFragment : Fragment(R.layout.fragment_done) {

    private lateinit var binding: FragmentDoneBinding
    private lateinit var exerciseDao: ExerciseDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseDao = ExercisesDatabase.getDatabase(requireActivity().application).exerciseDao()
        binding = FragmentDoneBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = getString(R.string.title_done_exercises)
        binding.doneRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        GlobalScope.launch(Dispatchers.IO){
            val pref: SharedPreferences = requireContext().applicationContext
                .getSharedPreferences(getString(R.string.myPref), 0)

            val minutesMap: Map<String, Int> = pref.all as Map<String, Int>
            val myExercises = exerciseDao.exercises
            val doneExercises = myExercises.filter { pref.contains(it.id.toString()) }

            var percent = 0.0;
            if (myExercises.isNotEmpty()){
                percent = (doneExercises.size.toDouble() / myExercises.size.toDouble()) * 100.0
            }

            withContext(Dispatchers.Main){
                binding.ivProgress.visibility = if (doneExercises.isNotEmpty()) View.GONE else View.VISIBLE
                binding.doneRecyclerView.adapter = DoneAdapter( doneExercises, minutesMap)
                binding.textProgress.text = "${percent.roundToInt()}%"
            }
        }
    }

}