package com.example.foodnote.ui.calorie_calculator_fragment.adapter.view_pager_adapter


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodnote.ui.calorie_calculator_fragment.const.FragmentIndex
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.CircleFragment
import com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments.composeUi.WaterFragmentCompose

class TotalViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments by lazy {
        arrayOf(CircleFragment(), WaterFragmentCompose())
    }

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int) =
        when (position) {
            FragmentIndex.CIRCLE_FRAGMENT.value.first -> CircleFragment()
            FragmentIndex.WATER_FRAGMENT.value.first -> fragments[FragmentIndex.WATER_FRAGMENT.value.first]
            else -> fragments[FragmentIndex.CIRCLE_FRAGMENT.value.first]
        }
}