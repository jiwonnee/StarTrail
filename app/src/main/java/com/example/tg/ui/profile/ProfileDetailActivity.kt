package com.example.tg.ui.profile

import android.os.Bundle
import com.example.tg.databinding.ActivityProfileDetailBinding
import com.example.tg.R
import android.content.Intent
import android.net.Uri
import com.example.tg.MainActivity

class ProfileDetailActivity : MainActivity() {

    private lateinit var binding: ActivityProfileDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val location = intent.getStringExtra("location")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.user_image)
        val introduction = intent.getStringExtra("introduction")
        val instagramUrl = intent.getStringExtra("instagramUrl")

        binding.textName.text = name
        binding.textPhone.text = phone
        binding.textEmail.text = email
        binding.textLocation.text = location
        binding.profileImage.setImageResource(imageResId)
        binding.textIntroduction.text = introduction
        binding.instagramLogo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
            startActivity(intent)
        }
        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.backButton.setOnClickListener {
            finish()
        }

        // 전화번호 클릭 시 다이얼 화면으로 이동
        binding.phoneLogo.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(dialIntent)
        }
    }
}
