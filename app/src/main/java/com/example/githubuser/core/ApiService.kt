package com.example.githubuser.core

import com.example.githubuser.model.UserDetailResponse
import com.example.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    fun fetchUsers(): Call<ArrayList<UsersResponse>>

    @GET("users/{login}")
    fun fetchDetailUser(): Call<UserDetailResponse>
}