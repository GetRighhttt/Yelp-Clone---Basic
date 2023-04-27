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
import com.example.yelpclone.data.model.yelp.YelpRestaurants
import com.example.yelpclone.databinding.YelpListItemBinding

class RestaurantsAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    companion object {

        private val callback = object
            : DiffUtil.ItemCallback<YelpRestaurants>() {

            override fun areItemsTheSame(
                oldItem: YelpRestaurants,
                newItem: YelpRestaurants
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: YelpRestaurants,
                newItem: YelpRestaurants
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
        fun bind(restaurant: YelpRestaurants) {
            binding.apply {
                tvTitle.text = restaurant.name
                tvAddress.text = restaurant.location.address1
                tvRating.rating = restaurant.rating.toFloat()
                tvPhoneNumber.text = restaurant.phone
                tvCity.text = "${restaurant.location.city}, "
                tvState.text = " ${restaurant.location.state}, "
                tvCountry.text = " ${restaurant.location.country}"
                tvZipcode.text = " ${restaurant.location.zipCode}"
                tvCategories.text = restaurant.categories[0].title
                tvDistance.text = restaurant.displayDistance() // converts to nice format
                tvPrice.text = restaurant.price
                tvReviews.text = "${restaurant.reviewCount} Reviews"

                // scale and transform image to our needs using Glide.
                Glide.with(ivYelpImage.context)
                    .load(restaurant.imageUrl)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(), RoundedCorners(20)
                        )
                    )
                    .into(ivYelpImage)
            }
        }

        // Needed for animation
        val yelpListItem = binding.yelpItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = YelpListItemBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = differ.currentList[position]
        holder.bind(restaurant)
        holder.yelpListItem.setOnClickListener {
            onClickListener.onItemClick(position)
        }
        holder.yelpListItem.startAnimation( // animation for recycler view
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.favorite_anim)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size
}