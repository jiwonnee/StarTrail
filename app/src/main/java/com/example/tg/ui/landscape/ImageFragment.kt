package com.example.tg.ui.landscape

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.request.CachePolicy
import com.example.tg.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val imageUrl = arguments?.getString(ARG_IMAGE)

        binding.imageView.load("file:///android_asset/$imageUrl") {
            crossfade(true)
            memoryCachePolicy(CachePolicy.ENABLED)
        }

        binding.imageView.setOnClickListener {
            // 클릭 시 정보를 보여주는 로직 추가
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_IMAGE = "image"

        fun newInstance(image: String) = ImageFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_IMAGE, image)
            }
        }
    }
}
