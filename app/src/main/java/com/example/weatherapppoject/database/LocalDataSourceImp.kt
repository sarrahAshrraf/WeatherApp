package com.example.weatherapppoject.database

import android.content.Context
import android.util.Log
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImp(context: Context): LocalDataSourceInte {

    private val dao : WeatherDao by lazy {
        val db : AppDB = AppDB.getInstance(context)
        db.getWeatherDAO()
    }

//favorite feature CRUD operations
    override suspend fun setFavoriteData(
        favorite: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        dao.setAsFavorite(favorite,longitude,latitude)
    }

    override suspend fun deleteFavData(weatherData: WeatherResponse) {
        Log.i("d======eeee","local data sour4e")
        dao.delete(weatherData)
//        dao.deleteFavByLonLat(longitude,latitude)
    }

    override  fun displayAllFav(): Flow<List<WeatherResponse>> {
        return dao.getFav()
    }

    override fun getCityData(longitude: Double, latitude: Double): Flow<WeatherResponse> {
        return dao.getSpecificCityData(longitude,latitude)
    }

    override suspend fun setALertData(alert: AlertData, longitude: Double, latitude: Double) {
        return dao.setAsAlerted(alert,longitude,latitude)
    }

    override suspend fun deleteAlertData(weatherAlertedData: AlertData) {
        dao.deleteAlertDataAll(weatherAlertedData)
    }

    override fun displayAllAlerts(): Flow<List<AlertData>> {
        return dao.getAlertsData()
    }

    override suspend fun insertAlert(alert: AlertData) {
        dao.insertAlert(alert)
    }
}





