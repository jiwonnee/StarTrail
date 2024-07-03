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
        val adapter = ReviewsPagerAdapter(this)
        viewPager.adapter = adapter
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
