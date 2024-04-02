package com.example.weatherapppoject.data

import com.example.weatherapppoject.onecall.model.AlertData
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataCourceImp (var homeAndfav : MutableList<WeatherResponse> = mutableListOf(),
                              val oneapi : MutableList<WeatherResponse> = mutableListOf(),
                              val alert : MutableList<AlertData> = mutableListOf() )
        : LocalDataSourceInte {




    override suspend fun setFavoriteData(
        favorite: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        homeAndfav.removeFirst()
        homeAndfav.add(favorite)
    }

    override suspend fun deleteFavData(weatherData: WeatherResponse) {
        homeAndfav.remove(weatherData)
    }

    override suspend fun deleteHomeData(weatherData: WeatherResponse) {
        TODO("Not yet implemented")
    }

    override fun displayAllFav(): Flow<List<WeatherResponse>>  = homeAndfav.let {
        return@let flowOf(it)
    }

    override fun getCityData(longitude: Double, latitude: Double): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHomeData(
        weatherData: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        homeAndfav.removeFirst()
        homeAndfav.add(weatherData)
    }

    override fun getCityDataHome(): Flow<WeatherResponse>  = homeAndfav.let {
        return@let it.asFlow()
    }

    override suspend fun deleteHomeData() {
        TODO("Not yet implemented")
    }

    override suspend fun setALertData(alert: AlertData, longitude: Double, latitude: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertData(weatherAlertedData: AlertData) {
        alert.remove(weatherAlertedData)
    }

    override fun displayAllAlerts(): Flow<List<AlertData>>  = alert.let{
        return@let flowOf(it)
    }

    override suspend fun insertAlert(dAlert: AlertData) {
        alert.add(dAlert)
    }


}

