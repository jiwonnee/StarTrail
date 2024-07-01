package com.example.tg.ui.landscape

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tg.databinding.FragmentLandscapeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class LandscapeFragment : Fragment() {

    private var _binding: FragmentLandscapeBinding? = null
    private val binding get() = _binding!!
    private val cameraRequestCode = 98
    private val storageRequestCode = 99
    private var imagePath: String? = null

    private val cameraPermissions = arrayOf(Manifest.permission.CAMERA)
    private val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

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
            if (checkAndRequestPermissions()) {
                openCamera()
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.extras?.get("data")?.let {
                    val img = it as Bitmap
                    val uri = saveFile(generateRandomFileName(), "image/jpeg", img)
                    // Update the ImageView with the new image URI (you need to handle this)
                    showAddLandscapeDialog(uri)
                }
            }
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var allPermissionsGranted = true
            var permanentlyDenied = false

            permissions.entries.forEach { entry ->
                if (!entry.value) {
                    allPermissionsGranted = false
                    if (!shouldShowRequestPermissionRationale(entry.key)) {
                        permanentlyDenied = true
                    }
                }
            }

            if (allPermissionsGranted) {
                openCamera()
            } else {
                if (permanentlyDenied) {
                    showSettingsDialog()
                } else {
                    Toast.makeText(requireContext(), "모든 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

        return root
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionsNeeded = mutableListOf<String>()

        cameraPermissions.forEach {
            if (ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(it)
            }
        }

        storagePermissions.forEach {
            if (ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(it)
            }
        }

        return if (permissionsNeeded.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
            false
        } else {
            true
        }
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

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            val imageFile: File? = try {
                createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
            imageFile?.let {
                val imageUri: Uri = FileProvider.getUriForFile(requireContext(), "com.example.tg.fileprovider", it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                cameraLauncher.launch(intent)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "JPEG_${timeStamp}_"
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir).apply {
            imagePath = absolutePath
        }
    }

    private fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri? {
        val contentValues = ContentValues()

        // MediaStore 에 파일명, mimeType 을 지정
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        // 안정성 검사
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // MediaStore 에 파일을 저장
        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val scriptor = requireContext().contentResolver.openFileDescriptor(it, "w")
            scriptor?.use { parcelFileDescriptor ->
                val fos = FileOutputStream(parcelFileDescriptor.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                // IS_PENDING 을 초기화
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                requireContext().contentResolver.update(it, contentValues, null, null)
            }
        }
        return uri
    }

    private fun generateRandomFileName(): String {
        return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(System.currentTimeMillis())
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
