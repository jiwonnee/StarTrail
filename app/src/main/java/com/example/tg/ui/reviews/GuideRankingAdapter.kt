package com.example.tg.ui.reviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tg.R
import com.example.tg.ui.reviews.Guide

class GuideRankingAdapter(private val guides: List<Guide>) :
    RecyclerView.Adapter<GuideRankingAdapter.GuideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guide_ranking, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guides[position]
        holder.nameTextView.text = guide.name
        holder.descriptionTextView.text = guide.description
        setStarRating(holder.starContainer, guide.rating)
        setRankImage(holder.rankImageView, position + 1)
    }

    override fun getItemCount(): Int = guides.size

    class GuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rankImageView: ImageView = view.findViewById(R.id.rankImageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val starContainer: ViewGroup = view.findViewById(R.id.starContainer)
    }

    private fun setStarRating(starContainer: ViewGroup, rating: Double) {
        starContainer.removeAllViews()
        val fullStars = (rating).toInt()
        val hasHalfStar = (rating % 1) >= 0.5
        val maxStars = 10

        for (i in 1..maxStars) {
            val starImageView = ImageView(starContainer.context)
            when {
                i <= fullStars -> starImageView.setImageResource(R.drawable.ic_star)
                i == fullStars + 1 && hasHalfStar -> starImageView.setImageResource(R.drawable.ic_star_half)
                else -> starImageView.setImageResource(R.drawable.ic_star_border)
            }
            starContainer.addView(starImageView)
        }
    }

    private fun setRankImage(imageView: ImageView, rank: Int) {
        val imageResId = when (rank) {
            1 -> R.drawable.ranking_1
            2 -> R.drawable.ranking_2
            3 -> R.drawable.ranking_3
            else -> {
                imageView.visibility = View.GONE  // 조건이 맞지 않으면 뷰를 사라지게 함
                return
            }
        }
        imageView.setImageResource(imageResId)
        imageView.visibility = View.VISIBLE  // 이미지가 있을 때만 보이도록 설정
    }
}

