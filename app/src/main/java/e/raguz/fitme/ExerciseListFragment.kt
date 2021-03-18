package e.raguz.fitme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import e.raguz.fitme.api.ApplicationApi
import e.raguz.fitme.database.ExercisesDatabase
import e.raguz.fitme.databinding.FragmentExercisesListBinding
import e.raguz.fitme.model.Exercise
import e.raguz.fitme.model.ExerciseDao
import e.raguz.fitme.model.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExerciseListFragment : Fragment(R.layout.fragment_exercises_list) {

    private var categoryId : Int = 0
    private lateinit var exerciseDao : ExerciseDao
    private var adapter: ExerciseAdapter = ExerciseAdapter { exercise -> navigateToDetail(exercise) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            categoryId  = it.getInt("id")
        }

        exerciseDao = ExercisesDatabase.getDatabase(requireActivity().application).exerciseDao()

        val binding = FragmentExercisesListBinding.bind(view)
        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.exerciseRecyclerView.adapter = adapter

        ApplicationApi.client.fetchExercises().enqueue(object: Callback<ResponseWrapper<Exercise>> {
            override fun onResponse(
                call: Call<ResponseWrapper<Exercise>>,
                response: Response<ResponseWrapper<Exercise>>
            ) {
                if (response.body() != null){
                    adapter.setData(response.body()!!.results.filter { it.category == categoryId })
                }
                GlobalScope.launch(Dispatchers.IO) {
                    val exercises = exerciseDao.exercises
                    withContext(Dispatchers.Main){
                        adapter.setAddedExercises(exercises)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseWrapper<Exercise>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }

        })

    }

    private fun navigateToDetail(exercise: Exercise) {
        val action = ExerciseListFragmentDirections.actionGlobalExerciseDetailFragment(exercise.id)
        findNavController().navigate(action)
    }
}