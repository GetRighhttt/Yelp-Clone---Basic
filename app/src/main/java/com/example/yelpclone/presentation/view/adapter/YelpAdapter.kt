package com.example.yelpclone.presentation.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yelpclone.R
import com.example.yelpclone.domain.model.yelp.YelpBusinesses
import com.example.yelpclone.databinding.YelpListItemBinding

class YelpBusinessAdapter(
    private val context: Context
) : RecyclerView.Adapter<YelpBusinessAdapter.ViewHolder>() {

    companion object {

        private val callback = object
            : DiffUtil.ItemCallback<YelpBusinesses>() {

            override fun areItemsTheSame(
                oldItem: YelpBusinesses,
                newItem: YelpBusinesses
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: YelpBusinesses,
                newItem: YelpBusinesses
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

    val differ = AsyncListDiffer(this, callback)

    inner class ViewHolder(
        private val binding: YelpListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(business: YelpBusinesses) {
            binding.apply {
                tvTitle.text = business.name
                tvAddress.text = business.location.address1
                tvRating.rating = business.rating.toFloat()
                tvPhoneNumber.text = business.phone
                tvCity.text = "${business.location.city}, "
                tvState.text = " ${business.location.state}, "
                tvCountry.text = " ${business.location.country}"
                tvZipcode.text = " ${business.location.zipCode}"
                tvCategories.text = business.categories[0].title
                tvDistance.text = business.displayDistance() // converts to nice format
                tvReviews.text = "${business.reviewCount} Reviews"

                // scale and transform image to our needs using Glide.
                Glide.with(ivYelpImage.context)
                    .load(business.imageUrl)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(), RoundedCorners(20)
                        )
                            .placeholder(R.drawable.baseline_food_bank_24)
                    )
                    .into(ivYelpImage)

                root.setOnClickListener{
                    onItemClickListener?.let {
                        it(business)
                    }
                }
            }
        }

        // Needed for animation
        val yelpListItem = binding.yelpItem
    }

    /*
    Item click listener variable.
    */
    private var onItemClickListener: ((YelpBusinesses) -> Unit)? = null

    /*
    Setter method for the onItemClickListener.
    */
    fun setOnItemClickListener(listener: ((YelpBusinesses) -> Unit)?) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = YelpListItemBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = differ.currentList[position]
        holder.bind(restaurant)
        holder.yelpListItem.startAnimation( // animation for recycler view
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.favorite_anim)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size
}