package com.example.tg.ui.landscape

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagePagerAdapter(fragment: Fragment, private val images: List<String>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = images.size

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(images[position])
    }
}
