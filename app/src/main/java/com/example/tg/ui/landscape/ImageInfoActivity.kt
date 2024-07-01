package com.example.tg.ui.landscape

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.tg.databinding.ActivityImageInfoBinding
import com.example.tg.util.ImageInfo
import com.example.tg.util.JsonUtil

class ImageInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra(ARG_IMAGE)
        val imageTitle = intent.getStringExtra(ARG_TITLE)

        Log.d(TAG, "Received imageUrl: $imageUrl, imageTitle: $imageTitle")

        if (imageUrl == null || imageTitle == null) {
            Log.e(TAG, "Image URL or Title is null")
            finish() // 종료 전 사용자에게 알림을 표시할 수도 있습니다.
            return
        }

        // JSON 파일에서 데이터 로드
        val imageInfoList = JsonUtil.loadImageInfo(this, "image_info.json")
        Log.d(TAG, "Loaded image info list: $imageInfoList")

        // 파일 이름과 imageTitle의 값을 로그로 확인
        for (info in imageInfoList) {
            Log.d(TAG, "fileName: ${info.fileName}, imageTitle: $imageTitle")
        }

        val imageInfo = imageInfoList.find { it.fileName.equals(imageTitle, ignoreCase = true) }
        Log.d(TAG, "Found image info: $imageInfo")

        if (imageInfo == null) {
            Log.e(TAG, "No description found for image title: $imageTitle")
            binding.imageDescription.text = "No description available."
        } else {
            binding.imageDescription.text = imageInfo.description
        }

        binding.imageView.load("file:///android_asset/$imageUrl")
        binding.imageTitle.text = imageTitle
    }

    companion object {
        private const val TAG = "ImageInfoActivity"
        const val ARG_IMAGE = "image"
        const val ARG_TITLE = "title"
    }
}
