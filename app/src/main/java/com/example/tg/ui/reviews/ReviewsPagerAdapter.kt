package com.example.tg.ui.reviews

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ReviewsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            0 -> GuideRankingsFragment()
//            1 -> PlaceRankingsFragment()
//            else -> throw IllegalStateException("Unexpected position $position")
//        }
        return GuideRankingsFragment()
    }
}
