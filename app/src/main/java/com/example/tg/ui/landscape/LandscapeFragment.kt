package com.example.tg.ui.landscape

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tg.databinding.FragmentLandscapeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import java.io.IOException

class LandscapeFragment : Fragment() {

    private var _binding: FragmentLandscapeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandscapeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cities = listOf("Seoul", "Busan", "Daejeon", "Incheon", "Jeju")

        // Initialize ViewPager for images with the first city's images
        val imageAdapter = ImagePagerAdapter(this, getImagesFromAssets(cities[0]))
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
                    val images = getImagesFromAssets(selectedCity)
                    binding.imageViewPager.adapter = ImagePagerAdapter(this@LandscapeFragment, images)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnAddLandscape.setOnClickListener {
            // You can add your action here for adding landscape
        }

        return root
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("권한 필요")
            .setMessage("앱 설정에서 권한을 부여해주세요.")
            .setPositiveButton("설정으로 이동") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAddLandscapeDialog(imageUri: Uri?) {
        val editText = EditText(requireContext())
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Landscape Name")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val name = editText.text.toString()
                // Save the image and name (this part needs to be implemented)
                // For example, add the new landscape to the cityImages map and update the adapter
            }
            .setNegativeButton("Cancel", null)
            .show()
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
