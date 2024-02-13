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
import com.example.yelpclone.domain.model.users.UserList
import com.example.yelpclone.databinding.UserListItemBinding


class UserAdapter(
    private val context: Context
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    companion object {

        private val callback = object
            : DiffUtil.ItemCallback<UserList>() {

            override fun areItemsTheSame(
                oldItem: UserList,
                newItem: UserList
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserList,
                newItem: UserList
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    val differ = AsyncListDiffer(this, callback)

    inner class ViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(user: UserList) {
            binding.apply {
                userFullName.text = "${user.firstname} ${user.lastName}"

                // scale and transform image to our needs using Glide.
                Glide.with(userImage.context)
                    .load(user.avatar)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(), RoundedCorners(20)
                        )
                            .placeholder(R.drawable.baseline_person_24)
                    )
                    .into(userImage)

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(user)
                    }
                }
            }
        }

        val userListItem = binding.userItem
    }

    /*
    Item click listener variable.
    */
    private var onItemClickListener: ((UserList) -> Unit)? = null

    /*
    Setter method for the onItemClickListener.
    */
    fun setOnItemClickListener(listener: ((UserList) -> Unit)?) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserListItemBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.bind(user)
        holder.userListItem.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.in_from_bottom)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size
}