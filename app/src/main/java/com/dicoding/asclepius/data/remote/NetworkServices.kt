package com.dicoding.asclepius.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${com.dicoding.asclepius.BuildConfig.API_KEY}")
                .build()
            chain.proceed(newRequest)
        })
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(com.dicoding.asclepius.BuildConfig.URL_WEB)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: HealtApiServices = retrofit.create(HealtApiServices::class.java)
}
