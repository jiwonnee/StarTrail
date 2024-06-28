package com.example.tg.ui.landscape

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tg.databinding.FragmentLandscapeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LandscapeFragment : Fragment() {

    private var _binding: FragmentLandscapeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandscapeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cities = listOf("Seoul", "New York", "Tokyo", "Paris", "London")
        val cityImages = mapOf(
            "Seoul" to listOf("Seoul1.jpeg", "Seoul2.jpeg", "Seoul3.jpeg", "Seoul4.jpeg"),
            "New York" to listOf("NewYork1.jpeg", "NewYork2.jpeg", "NewYork3.jpeg", "NewYork4.jpeg"),
            "Tokyo" to listOf("Tokyo1.jpeg", "Tokyo2.jpeg", "Tokyo3.jpeg", "Tokyo4.jpeg"),
            "Paris" to listOf("Paris1.jpeg", "Paris2.jpeg", "Paris3.jpeg", "Paris4.jpeg"),
            "London" to listOf("London1.jpeg", "London2.jpeg", "London3.jpeg", "London4.jpeg")
        )

        // Initialize ViewPager for images with the first city's images
        val imageAdapter = ImagePagerAdapter(this, cityImages[cities[0]] ?: emptyList())
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
                    val images = cityImages[selectedCity] ?: emptyList()
                    binding.imageViewPager.adapter = ImagePagerAdapter(this@LandscapeFragment, images)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
