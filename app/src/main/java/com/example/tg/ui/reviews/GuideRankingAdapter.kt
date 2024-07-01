package com.example.tg.ui.reviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tg.R

class GuideRankingAdapter(private var guides: List<Guide>) :
    RecyclerView.Adapter<GuideRankingAdapter.GuideViewHolder>() {

    private var filteredGuides: List<Guide> = guides

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guide_ranking, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = filteredGuides[position]
        holder.nameTextView.text = guide.name
        holder.descriptionTextView.text = guide.description
        setStarRating(holder.starContainer, guide.rating)
    }

    override fun getItemCount(): Int = filteredGuides.size

    class GuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val starContainer: ViewGroup = view.findViewById(R.id.starContainer)
    }

    private fun setStarRating(starContainer: ViewGroup, rating: Double) {
        starContainer.removeAllViews()
        val fullStars = rating.toInt()
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

    fun updateList(newList: List<Guide>) {
        filteredGuides = newList
        notifyDataSetChanged()
    }
}
