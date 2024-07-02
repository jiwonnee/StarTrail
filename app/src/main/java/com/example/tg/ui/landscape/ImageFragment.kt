package com.example.tg.ui.landscape

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import coil.load
import coil.request.CachePolicy
import com.example.tg.R
import com.example.tg.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var isFavorite = false
    private lateinit var currentImageUrl: String
    private lateinit var currentCity: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
        currentImageUrl = arguments?.getString(ARG_IMAGE) ?: ""
        currentCity = arguments?.getString(ARG_CITY) ?: ""

        // 파일 이름에서 확장자를 제거한 후 설정
        val fileName = currentImageUrl.substringAfterLast('/').substringBeforeLast('.')
        binding.imageTitle.text = fileName

        binding.imageView.load("file:///android_asset/$currentImageUrl") {
            crossfade(true)
            memoryCachePolicy(CachePolicy.ENABLED)
        }

        isFavorite = checkIfFavorite(currentCity, currentImageUrl)
        updateStarIcon()

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteStatus(currentCity, currentImageUrl, isFavorite)
            updateStarIcon()
        }

        binding.imageView.setOnClickListener {
            val intent = Intent(context, ImageInfoActivity::class.java).apply {
                putExtra(ImageInfoActivity.ARG_IMAGE, currentImageUrl)
                putExtra(ImageInfoActivity.ARG_TITLE, fileName)
            }
            startActivity(intent)
        }
        return binding.root
    }

    private fun updateStarIcon() {
        if (isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.ic_star2)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_star_border2)
        }
    }

    private fun updateFavoriteStatus(city: String, imageUrl: String, isFavorite: Boolean) {
        val favoritesKey = "${KEY_FAVORITES}_$city"
        val favorites = sharedPreferences.getStringSet(favoritesKey, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        if (isFavorite) {
            favorites.add(imageUrl)
            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
        } else {
            favorites.remove(imageUrl)
            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
        }
        sharedPreferences.edit { putStringSet(favoritesKey, favorites) }
    }

    private fun checkIfFavorite(city: String, imageUrl: String): Boolean {
        val favoritesKey = "${KEY_FAVORITES}_$city"
        val favorites = sharedPreferences.getStringSet(favoritesKey, emptySet()) ?: return false
        return favorites.contains(imageUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_IMAGE = "image"
        private const val ARG_CITY = "city"
        const val KEY_FAVORITES = "favorites"

        fun newInstance(image: String, city: String) = ImageFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_IMAGE, image)
                putString(ARG_CITY, city)
            }
        }
    }
}
