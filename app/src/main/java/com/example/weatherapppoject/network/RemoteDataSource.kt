package com.example.weatherapppoject.network

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow


interface RemoteDataSource {
//    suspend fun getWeatherINfo(
//        latitude: Double,
//        longitude: Double,
//        units: String,
//        apiKey: String
//    ): Flow<WeatherList>

    suspend fun getFiveDaysInfo(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse>
    suspend fun getALerts(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<OneApiCall>
}