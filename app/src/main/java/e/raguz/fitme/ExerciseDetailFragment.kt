package e.raguz.fitme

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import e.raguz.fitme.api.ApplicationApi
import e.raguz.fitme.database.ExercisesDatabase
import e.raguz.fitme.databinding.FragmentExerciseDetailBinding
import e.raguz.fitme.model.Exercise
import e.raguz.fitme.model.ExerciseDao
import e.raguz.fitme.model.ExerciseDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExerciseDetailFragment : Fragment(R.layout.fragment_exercise_detail) {

    private val args: ExerciseDetailFragmentArgs by navArgs()
    private lateinit var exercise: Exercise
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var binding : FragmentExerciseDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseDao = ExercisesDatabase.getDatabase(requireActivity().application).exerciseDao()
        binding = FragmentExerciseDetailBinding.bind(view)

        val id: Int = args.exerciseId

        ApplicationApi.client.fetchExerciseDetails(id).enqueue(object : Callback<ExerciseDetail> {
            override fun onResponse(
                call: Call<ExerciseDetail>,
                response: Response<ExerciseDetail>
            ) {
                if (response.body() != null) {
                    val data = response.body()!!
                    (requireActivity() as MainActivity).supportActionBar?.title = data.name
                    (requireActivity() as MainActivity).supportActionBar?.setHomeButtonEnabled(true)
                    binding.textViewEquipment.text =
                        data.equipment.joinToString(separator = "") { "${it.name}\n" }
                    val muscles = data.muscles + data.muscles_secondary
                    binding.textViewMuscles.text =
                        muscles.joinToString(separator = "") { "${it.name}\n" }
                    binding.textViewComments.text = data.comments
                        .joinToString(separator = "") { "\"${it.comment}\" \n" }

                    exercise = Exercise(id, data.category.id, data.description, data.name, data.name, listOf(), listOf(), listOf(), data.creation_date,
                    data.language.id, data.uuid, data.variations.toString())

                    checkIfExisting(id)
                }
            }

            override fun onFailure(call: Call<ExerciseDetail>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }

        })

        binding.buttonAddExercise.setOnClickListener {
           GlobalScope.launch(Dispatchers.IO){exerciseDao.insertExercise(exercise)}
            binding.buttonAddExercise.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), "You added this exercise in ToDo list!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfExisting(id: Int) {
        GlobalScope.launch(Dispatchers.IO){
           val exercise = exerciseDao.findExerciseById(id)
            if (exercise == null){
                withContext(Dispatchers.Main){
                    binding.buttonAddExercise.visibility = View.VISIBLE
                }

            }
        }
    }
}