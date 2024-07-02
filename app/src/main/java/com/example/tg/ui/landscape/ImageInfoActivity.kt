package com.example.tg.ui.landscape

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.tg.MainActivity
import com.example.tg.R
import com.example.tg.databinding.ActivityImageInfoBinding
import com.example.tg.util.ImageInfo
import com.example.tg.util.JsonUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ImageInfoActivity : MainActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityImageInfoBinding
    private lateinit var mMap: GoogleMap
    private var imageInfo: ImageInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra(ARG_IMAGE)
        val imageTitle = intent.getStringExtra(ARG_TITLE)

        Log.d(TAG, "Received imageUrl: $imageUrl, imageTitle: $imageTitle")

        if (imageUrl == null || imageTitle == null) {
            Log.e(TAG, "Image URL or Title is null")
            finish()
            return
        }

        // JSON 파일에서 데이터 로드
        val imageInfoList = JsonUtil.loadImageInfo(this, "image_info.json")
        Log.d(TAG, "Loaded image info list: $imageInfoList")

        // 파일 이름과 imageTitle의 값을 로그로 확인
        for (info in imageInfoList) {
            Log.d(TAG, "fileName: ${info.fileName}, imageTitle: $imageTitle")
        }

        imageInfo = imageInfoList.find { it.fileName.equals(imageTitle, ignoreCase = true) }
        Log.d(TAG, "Found image info: $imageInfo")

        if (imageInfo == null) {
            Log.e(TAG, "No description found for image title: $imageTitle")
            binding.imageDescription.text = "No description available."
        } else {
            binding.imageDescription.text = imageInfo!!.description
        }

        binding.imageView.load("file:///android_asset/$imageUrl")
        binding.imageTitle.text = imageTitle

        // SupportMapFragment 초기화 및 콜백 설정
        try {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing map fragment: ${e.message}", e)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "Map is ready")
        mMap = googleMap

        try {
            // 이미지 정보에 위치가 포함된 경우 지도에 마커 추가
            imageInfo?.let {
                val location = LatLng(it.latitude, it.longitude)
                Log.d(TAG, "Setting marker at location: $location")
                mMap.addMarker(MarkerOptions().position(location).title(it.fileName))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding marker to map: ${e.message}", e)
        }
    }

    companion object {
        private const val TAG = "ImageInfoActivity"
        const val ARG_IMAGE = "image"
        const val ARG_TITLE = "title"
    }
}