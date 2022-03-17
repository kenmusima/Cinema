package com.ken.cinema.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ken.cinema.ui.fragment.ContactFragment
import com.ken.cinema.ui.fragment.MovieFragment
import com.ken.cinema.ui.fragment.OrderFragment

private const val NUM_TABS = 3

class ViewPagerAdapter(fa: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fa,lifecycle) {
    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MovieFragment()
            1 -> ContactFragment()
            2 -> OrderFragment()
            else -> throw Throwable("Invalid position $position")
        }

    }



}