package com.example.weatherapppoject.network

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("weather?")
    suspend fun getCureentWeather(
        @Query("q") city : String,
        @Query("units") units : String,
        @Query("appid") apiKey : String,

        ) : WeatherList

    @GET("forecast?")
    suspend fun getForeCast(
        @Query("q") city : String,
        @Query("units") units : String,
        @Query("appid") apiKey : String,
        @Query("lang") lang :String

        ) : WeatherResponse
}



//3 hrs and 5 days
////////////daily => preception forecast
//    forecast.precipitation.unit Period of measurements. Possible value is 1 hour, 3 hours

//    @GET("onecall?appid=${Utils.APIKEY}&execlude=minutely")
//    suspend fun getWeatherInfo(
//    @Query("lat") lat: Double,
//    @Query("lon") lon: Double,
//    @Query("units") units: String,
//    @Query("lang") lang:String
//    ) : WeatherResponse
