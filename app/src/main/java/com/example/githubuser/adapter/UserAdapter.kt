package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ItemUsersBinding
import com.example.githubuser.model.UsersResponseItem

class UserAdapter(private val listUser: ArrayList<UsersResponseItem>) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.tvUsername.text = listUser[position].login
        holder.binding.tvType.text = listUser[position].type
        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .circleCrop()
            .into(holder.binding.imgAvatar)
    }

    override fun getItemCount(): Int = listUser.size
}