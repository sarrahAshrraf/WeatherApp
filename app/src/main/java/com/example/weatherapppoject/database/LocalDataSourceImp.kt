package com.example.weatherapppoject.database

import android.content.Context
import android.util.Log
import com.example.weatherapppoject.forecastmodel.WeatherResponse
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

    override suspend fun deleteFavData(longitude: Double, latitude: Double) {
        Log.i("d======eeee","local data sour4e")

        dao.deleteFavByLonLat(longitude,latitude)
    }

    override  fun displayAllFav(): Flow<List<WeatherResponse>> {
        return dao.getFav()
    }
}





