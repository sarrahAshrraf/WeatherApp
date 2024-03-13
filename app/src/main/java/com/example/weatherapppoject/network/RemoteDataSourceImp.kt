package com.example.weatherapppoject.network

import com.example.weatherapppoject.Utils
import com.example.weatherapppoject.Utils.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object RetrofitInstance {
    val wetherAPi: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}
