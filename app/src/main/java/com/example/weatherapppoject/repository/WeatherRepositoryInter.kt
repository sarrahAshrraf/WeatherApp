package com.example.weatherapppoject.repository

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepositoryInter {
//    suspend fun getCurrentWeather(
//        latitude: Double,
//        longitude: Double,
//        units: String,
//        apiKey: String
//    ): Flow<WeatherList>

    //network opreation
    suspend fun getFiveDaysWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse>

    //database operation
     fun getFavoriteData(): Flow<List<WeatherResponse>>
     suspend fun insertfavIntoDB(fav: WeatherResponse, longitude: Double, latitude: Double)
     suspend fun deleteFromFav(weatherData: WeatherResponse)

}