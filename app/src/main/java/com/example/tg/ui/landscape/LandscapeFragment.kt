package com.example.tg.ui.landscape

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tg.R
import com.example.tg.databinding.FragmentLandscapeBinding
import com.google.android.material.tabs.TabLayout
import java.io.IOException

class LandscapeFragment : Fragment() {

    private var _binding: FragmentLandscapeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var showFavorites = false
    private val cities = listOf("Seoul", "Busan", "Daejeon", "Incheon", "Jeju")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandscapeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("favorites_prefs", 0)

        // Initialize ViewPager for images with the first city's images
        val imageAdapter = ImagePagerAdapter(this, getImagesFromAssets(cities[0]), cities[0])
        binding.imageViewPager.adapter = imageAdapter
        binding.imageViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        // Set up TabLayout with cities
        cities.forEach { city ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(city))
        }

        // Change images when a different city is selected
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val selectedCity = cities[it.position]
                    updateImages(selectedCity)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnFavoriteFilter.setOnClickListener {
            showFavorites = !showFavorites
            val selectedCity = cities[binding.tabLayout.selectedTabPosition]
            updateImages(selectedCity)
            if (showFavorites) {
                binding.btnFavoriteFilter.text = "Show All"
            } else {
                binding.btnFavoriteFilter.text = "Show Favorites"
            }
        }

        return root
    }

    private fun updateImages(city: String) {
        val images = if (showFavorites) getFavoriteImages(city) else getImagesFromAssets(city)
        if (images.isEmpty()) {
            binding.emptyFavoritesText.visibility = View.VISIBLE
            binding.imageViewPager.visibility = View.GONE
        } else {
            binding.emptyFavoritesText.visibility = View.GONE
            binding.imageViewPager.visibility = View.VISIBLE
            binding.imageViewPager.adapter = ImagePagerAdapter(this, images, city)
        }
    }

    private fun getFavoriteImages(city: String): List<String> {
        val favorites = sharedPreferences.getStringSet("${ImageFragment.KEY_FAVORITES}_$city", emptySet()) ?: return emptyList()
        return favorites.toList()
    }

    private fun getImagesFromAssets(city: String): List<String> {
        val assetManager = requireContext().assets
        val images = mutableListOf<String>()

        try {
            val files = assetManager.list(city) ?: arrayOf()
            for (fileName in files) {
                images.add("$city/$fileName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return images
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
