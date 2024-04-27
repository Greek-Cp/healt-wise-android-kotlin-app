package com.dicoding.asclepius.data.remote

import com.dicoding.asclepius.data.model.ArticlesItem
import com.dicoding.asclepius.data.model.ResponseHealtModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HealtApiServices {
    @GET("top-headlines")
    fun getTopHealthNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): Call<ResponseHealtModel>
}
