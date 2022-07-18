package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.databinding.ItemUsersBinding
import com.example.githubuser.helper.FavoriteUsersDiffCallback
import com.example.githubuser.model.UsersResponse

class FavoriteUsersAdapter : RecyclerView.Adapter<FavoriteUsersAdapter.FavoriteUsersViewHolder>() {
    private val listFavoriteUsers = ArrayList<FavoriteUsers>()
//    private lateinit var onItemClickCallback: UserAdapter.OnItemClickCallback

    fun setListFavoriteUsers(listFavoriteUsers: List<FavoriteUsers>) {
        val diffCallback = FavoriteUsersDiffCallback(this.listFavoriteUsers, listFavoriteUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUsers.clear()
        this.listFavoriteUsers.addAll(listFavoriteUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUsersViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteUsersViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])
    }

    override fun getItemCount(): Int {
        return listFavoriteUsers.size
    }

    inner class FavoriteUsersViewHolder(private val binding: ItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUsers: FavoriteUsers) {
//            val users = UsersResponse(favoriteUsers)
            with(binding) {
                tvUsername.text = favoriteUsers.login
                tvType.text = favoriteUsers.type
//                Glide.with(itemView.context)
//                    .load(favoriteUsers.avatar_url)
//                    .circleCrop()
//                    .into(imgAvatar)
//                itemView.setOnClickListener { onItemClickCallback.onItemClicked(favoriteUsers)}
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UsersResponse)
    }
}