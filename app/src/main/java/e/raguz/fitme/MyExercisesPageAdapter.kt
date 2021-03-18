package e.raguz.fitme

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyExercisesPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ToDoFragment()
            1 -> DoneFragment()
            else-> ToDoFragment()
        }
    }
}