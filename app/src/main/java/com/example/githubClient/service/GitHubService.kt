package com.example.githubClient.service

import com.example.githubClient.model.Repo
import com.example.githubClient.model.User
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Observable<ArrayList<Repo>>

    @GET("users/{user}")
    fun getUser(@Path("user") user: String): Observable<User>
}