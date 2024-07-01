package com.example.tg.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tg.databinding.FragmentReviewsBinding
import com.google.android.material.tabs.TabLayoutMediator

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewPager = binding.viewPager


        // ViewPager2 어댑터 설정 - 0번 인덱스(Guide Rankings)만 표시
        val adapter = ReviewsPagerAdapter(this)
        viewPager.adapter = adapter

//        val tabLayout = binding.tabLayout
//
//        // ViewPager2 어댑터 설정
//        val adapter = ReviewsPagerAdapter(this)
//        viewPager.adapter = adapter
//
//        // TabLayout과 ViewPager2를 연결
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = when (position) {
//                0 -> "Guide Rankings"
//                1 -> "Place Rankings"
//                else -> null
//            }
//        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
