package com.example.weatherapppoject.network

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("2.5/forecast?")
    suspend fun getForeCast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String

    ): WeatherResponse

//for alerts data
    @GET("3.0/onecall")
    suspend fun getAlerts(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String

    ): OneApiCall

}
