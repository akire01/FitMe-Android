package e.raguz.fitme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import e.raguz.fitme.databinding.FragmentMyExercisesBinding


class MyExercisesFragment : Fragment(R.layout.fragment_my_exercises) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMyExercisesBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = "My Exercises"

        val pagerAdapter = MyExercisesPageAdapter(this)
        binding.viewPagerMy.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutMy, binding.viewPagerMy) { tab, position ->
            when(position){
                0 -> tab.text = "To Do"
                1-> tab.text = "Done"
                else -> tab.text = "To Do"
            }
        }.attach()
    }

}