package com.example.weatherapppoject.repository

import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface WeatherRepositoryInter {

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
     suspend fun deleteFromHome(weatherData: WeatherResponse)

    fun getFavCityInfo(longitude: Double, latitude: Double): Flow<WeatherResponse>
      suspend fun insertHomeData(weatherData: WeatherResponse,longitude: Double, latitude: Double)
    fun getFavCityInfoHome(): Flow<WeatherResponse>
    suspend fun deleteHome()

    ////=>local + alert

    fun getAlertedData(): Flow<List<AlertData>>
    suspend fun insertAlertIntoDB(alerts: AlertData, longitude: Double, latitude: Double)
    suspend fun insertAlerts(alert : AlertData)
    suspend fun deleteFromAlerts(alertWeatherData: AlertData)

}