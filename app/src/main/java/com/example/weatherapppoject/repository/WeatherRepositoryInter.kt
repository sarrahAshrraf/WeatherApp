package com.example.weatherapppoject.repository

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
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

    suspend fun getAlertData(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<OneApiCall>


    //database operation
     fun getFavoriteData(): Flow<List<WeatherResponse>>
     suspend fun insertfavIntoDB(fav: WeatherResponse, longitude: Double, latitude: Double)
     suspend fun deleteFromFav(weatherData: WeatherResponse)
      fun getFavCityInfo(longitude: Double, latitude: Double): Flow<WeatherResponse>

      ////=>local + alert

    fun getAlertedData(): Flow<List<OneApiCall>>
    suspend fun insertAlertIntoDB(alerts: OneApiCall, longitude: Double, latitude: Double)
    suspend fun deleteFromAlerts(alertWeatherData: OneApiCall)

}