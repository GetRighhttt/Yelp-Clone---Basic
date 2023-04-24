package com.example.yelpclone.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yelpclone.R
import com.example.yelpclone.data.model.YelpRestaurants
import com.example.yelpclone.databinding.YelpListItemBinding

class RestaurantsAdapter(
    private val context: Context
) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    companion object {

        private val callback = object
            : DiffUtil.ItemCallback<YelpRestaurants>() {

            override fun areItemsTheSame(oldItem: YelpRestaurants, newItem: YelpRestaurants): Boolean {
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

        fun bind(restaurant: YelpRestaurants) {
            binding.apply {
                tvTitle.text = restaurant.name
                tvAddress.text = restaurant.location.address1
                tvRating.text = restaurant.rating.toString()
                tvPhoneNumber.text = restaurant.phone
                tvCity.text = restaurant.location.city
                tvCountry.text = restaurant.location.country
                tvState.text = restaurant.location.state

                Glide.with(ivYelpImage.context)
                    .load(restaurant.imageUrl)
                    .into(ivYelpImage)

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(restaurant)
                    }
                }
            }
        }

        // Needed for animation
        val yelpListItem = binding.yelpItem
    }

    private var onItemClickListener: ((YelpRestaurants) -> Unit)? = null

    fun setOnItemClickListener(listener: ((YelpRestaurants) -> Unit)?) {
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