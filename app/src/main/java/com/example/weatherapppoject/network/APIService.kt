package com.example.weatherapppoject.network

import com.example.weatherapppoject.pojo.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("onecall?appid=3f2c5a9a086fa7d7056043da97b35aae&execlude=minutely")
    suspend fun getWeatherInfo(
    @Query("lat") lat: Double,
    @Query("lon") lon: Double,
    @Query("units") units: String,
    @Query("lang") lang:String
    ) : WeatherResponse
}