package com.example.weatherapppoject.database

import com.example.weatherapppoject.onecall.model.AlertData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInte {
    suspend fun setFavoriteData (favorite: WeatherResponse,longitude: Double,latitude: Double)
    suspend fun deleteFavData (weatherData: WeatherResponse)
    suspend fun deleteHomeData (weatherData: WeatherResponse)

    fun displayAllFav(): Flow<List<WeatherResponse>>
     fun getCityData(longitude: Double, latitude: Double): Flow<WeatherResponse>
     suspend fun insertHomeData(weatherData: WeatherResponse,longitude: Double, latitude: Double)
    fun getCityDataHome ():  Flow<WeatherResponse>
    suspend fun deleteHomeData()
     //================ALerts

    suspend fun setALertData (alert: AlertData, longitude: Double, latitude: Double)
    suspend fun deleteAlertData (weatherAlertedData: AlertData)
    fun displayAllAlerts(): Flow<List<AlertData>>
    suspend fun insertAlert(alert: AlertData)


}