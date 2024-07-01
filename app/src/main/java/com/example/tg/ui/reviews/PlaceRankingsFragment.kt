package com.example.tg.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tg.databinding.FragmentPlaceRankingsBinding

class PlaceRankingsFragment : Fragment() {

    private var _binding: FragmentPlaceRankingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceRankingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 장소 랭킹을 표시하는 로직을 여기에 추가

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
