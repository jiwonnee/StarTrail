package com.example.tg.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tg.databinding.ActivityProfileDetailBinding
import com.example.tg.R


class ProfileDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val location = intent.getStringExtra("location")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.user_image)

        binding.textName.text = name
        binding.textPhone.text = phone
        binding.textEmail.text = email
        binding.textLocation.text = location
        binding.profileImage.setImageResource(imageResId)
    }
}
