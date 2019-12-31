package com.example.githubClient.service

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GithubApi {

    companion object {
        private var retrofit: Retrofit? = null
        private val BASE_URL = "https://api.github.com/"

        fun getRetrofitInstance(): Retrofit = when (retrofit) {
            null -> {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                retrofit as Retrofit
            }
            else -> retrofit as Retrofit
        }
    }
}