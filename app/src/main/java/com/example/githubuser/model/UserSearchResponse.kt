package com.example.githubuser.model

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(

    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @field:SerializedName("items")
    val items: ArrayList<UsersResponse>? = null
)
