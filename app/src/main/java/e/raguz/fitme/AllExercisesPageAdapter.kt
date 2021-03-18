package e.raguz.fitme

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import e.raguz.fitme.model.Category

class AllExercisesPageAdapter(fragment: Fragment, private val categories: List<Category>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {

        return ExerciseListFragment().apply {
            arguments = Bundle().apply {
                putInt("id", categories[position].id)
            }
        }
    }

}