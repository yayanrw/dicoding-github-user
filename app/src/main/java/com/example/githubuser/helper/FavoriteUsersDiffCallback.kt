package com.example.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuser.database.FavoriteUsers

class FavoriteUsersDiffCallback(
    private val mOldFavoriteUsersList: List<FavoriteUsers>,
    private val mNewFavoriteUsersList: List<FavoriteUsers>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavoriteUsersList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavoriteUsersList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteUsersList[oldItemPosition].login == mNewFavoriteUsersList[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavoriteUsers = mOldFavoriteUsersList[oldItemPosition]
        val newFavoriteUsers = mNewFavoriteUsersList[newItemPosition]
        return oldFavoriteUsers.login == newFavoriteUsers.login && oldFavoriteUsers.avatar_url == newFavoriteUsers.avatar_url
    }
}