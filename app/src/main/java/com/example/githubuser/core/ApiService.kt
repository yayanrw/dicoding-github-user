package com.example.githubuser.core

import com.example.githubuser.model.UserDetailResponse
import com.example.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    fun fetchUsers(): Call<ArrayList<UsersResponse>>

    @GET("users/{login}")
    fun fetchUserDetail(
        @Path("login") login: String
    ): Call<UserDetailResponse>
}