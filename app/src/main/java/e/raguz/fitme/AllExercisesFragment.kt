package e.raguz.fitme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import e.raguz.fitme.api.ApplicationApi
import e.raguz.fitme.databinding.FragmentAllExercisesBinding
import e.raguz.fitme.model.Category
import e.raguz.fitme.model.ResponseWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllExercisesFragment : Fragment(R.layout.fragment_all_exercises) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAllExercisesBinding.bind(view)

        (requireActivity() as MainActivity).supportActionBar?.title = getString(R.string.title_all_Exercises)
        ApplicationApi.client.fetchCategories().enqueue(object: Callback<ResponseWrapper<Category>> {
            override fun onResponse(
                call: Call<ResponseWrapper<Category>>,
                response: Response<ResponseWrapper<Category>>
            ) {
                if (response.body() != null){
                    val data = response.body()!!
                    val categories = data.results.subList(0, 5)

                    val pagerAdapter = AllExercisesPageAdapter(this@AllExercisesFragment, categories)
                    binding.viewPager.adapter = pagerAdapter

                    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                        tab.text = categories[position].name
                    }.attach()
                }
            }
            override fun onFailure(call: Call<ResponseWrapper<Category>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }
}