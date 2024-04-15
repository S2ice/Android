package com.example.talki.ui.identification.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.talki.ui.identification.fragment.CategoryFragment
import com.example.talki.ui.identification.fragment.NameFragment

class ViewPagerFragmentAdapter(
    fragmentActivity: FragmentActivity
):
    FragmentStateAdapter(fragmentActivity){

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NameFragment()
            1 -> CategoryFragment()
            else -> throw IllegalArgumentException("Неверный счет: $position")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}