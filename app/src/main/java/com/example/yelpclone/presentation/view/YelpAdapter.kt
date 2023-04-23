package com.example.yelpclone.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yelpclone.R
import com.example.yelpclone.data.model.YelpRestaurants
import com.example.yelpclone.databinding.YelpListItemBinding

class RestaurantsAdapter(
    private val context: Context
) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    /*----------------------------------------------------------------------------
    DiffUtil reduces the number of updates for converting one list to another.
    We always pass in our list of data for the item call back.
    Class has two override methods that we will use to update the list.
     */
    private val callback = object
        : DiffUtil.ItemCallback<YelpRestaurants>() {

        override fun areItemsTheSame(oldItem: YelpRestaurants, newItem: YelpRestaurants): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: YelpRestaurants,
            newItem: YelpRestaurants
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    /*
    ----------------------------------------------------------------------------
    End DiffUtil -> Start ViewHolder()

    Now we define our view holder. This is where we bind our views with the
    recycler view. We can modify the recycler view items here - onClicks, etc.
    ----------------------------------------------------------------------------
     */

    inner class ViewHolder(
        private val binding: YelpListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: YelpRestaurants) {
            binding.apply {
                tvTitle.text = restaurant.name
                tvAddress.text = restaurant.location.address1
                tvRating.text = restaurant.rating.toString()
                tvPhoneNumber.text = restaurant.phone

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

    /*----------------------------------------------------------------------------
    The methods defined below are going to be used to set an item click listener for the list.
    */

    private var onItemClickListener: ((YelpRestaurants) -> Unit)? = null

    fun setOnItemClickListener(listener: ((YelpRestaurants) -> Unit)?) {
        onItemClickListener = listener
    }
    /*
    ----------------------------------------------------------------------------
    Now we define our implemented methods.
    ----------------------------------------------------------------------------
     */

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