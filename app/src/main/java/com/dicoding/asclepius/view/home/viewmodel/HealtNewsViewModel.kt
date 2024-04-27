package com.dicoding.asclepius.view.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.model.ArticlesItem
import com.dicoding.asclepius.data.model.ResponseHealtModel
import com.dicoding.asclepius.data.remote.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HealthNewsViewModel : ViewModel() {
     val articles = MutableLiveData<List<ArticlesItem>>()

    fun getArticles(): LiveData<List<ArticlesItem>> {
        return articles
    }

    fun fetchArticles() {
         NetworkService.apiService.getTopHealthNews("id","health",BuildConfig.API_KEY).enqueue(object : Callback<ResponseHealtModel> {
            override fun onResponse(call: Call<ResponseHealtModel>, response: Response<ResponseHealtModel>) {

                if (response.isSuccessful) {
                    // Filter out null articles before posting to LiveData
                    val articlesResponse = response.body()?.articles?.filterNotNull()
                    articles.postValue(articlesResponse ?: emptyList())  // Post an empty list if articlesResponse is null
                } else {
                    // Log or handle error response

                }
            }

            override fun onFailure(call: Call<ResponseHealtModel>, t: Throwable) {
                Log.e("Load Gagal","error ${t.message}");
            }
         })
    }
}

