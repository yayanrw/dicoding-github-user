package com.example.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.databinding.ItemUsersBinding

class FavoriteUsersAdapter : RecyclerView.Adapter<FavoriteUsersAdapter.MyViewHolder>() {
    private val listFavoriteUsers = ArrayList<FavoriteUsers>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListFavorite(items: List<FavoriteUsers>) {
        listFavoriteUsers.clear()
        listFavoriteUsers.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listFavoriteUsers.size
    }

    inner class MyViewHolder(private val binding: ItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUsers: FavoriteUsers) {
            with(binding) {
                tvUsername.text = favoriteUsers.login
                tvType.text = favoriteUsers.type
                Glide.with(itemView.context)
                    .load(favoriteUsers.avatar_url)
                    .circleCrop()
                    .into(binding.imgAvatar)
                itemView.setOnClickListener { onItemClickCallback.onItemClicked(favoriteUsers) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUsers)
    }
}