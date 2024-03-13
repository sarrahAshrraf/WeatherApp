package com.example.weatherapppoject.network

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.Utils
import com.example.weatherapppoject.pojo.Current
import com.example.weatherapppoject.pojo.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
//    @GET("onecall?appid=${Utils.APIKEY}&execlude=minutely")
//    suspend fun getWeatherInfo(
//    @Query("lat") lat: Double,
//    @Query("lon") lon: Double,
//    @Query("units") units: String,
//    @Query("lang") lang:String
//    ) : WeatherResponse

    @GET("weather?")
    suspend fun getCureentWeather(
        @Query("q") city : String,
        @Query("units") units : String,
        @Query("appid") apiKey : String,

        ) : Response<WeatherList>
}