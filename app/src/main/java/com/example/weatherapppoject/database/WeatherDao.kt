package com.example.weatherapppoject.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    //insert the weather data when the network fetch them
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherData: WeatherResponse)
//    =============>
    //get only the data in teh home fragment when no network
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT * FROM weather_data where isFav =0 LIMIT 1")
    @Query("SELECT * FROM weather_data where isFav =0 ORDER BY id DESC LIMIT 1")
    fun getWeatherData(): Flow<WeatherResponse>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(favorite: WeatherResponse)
    @Transaction
    suspend fun setAsFavorite(favorite: WeatherResponse, longitude: Double, latitude: Double) {
        favorite.isFav = 1
        favorite.latitude = latitude
        favorite.longitude = longitude
        Log.i("====db set", "setAsFavorite: ")
        insertFav(favorite)
    }


    @Transaction
    suspend fun setHomeStore(favorite: WeatherResponse, longitude: Double, latitude: Double) {
        favorite.isFav = 0
        favorite.latitude = latitude
        favorite.longitude = longitude
        Log.i("====db set", "setAsFavorite: ")
        insertWeatherData(favorite)
    }

    @Delete
    suspend fun deleteFav(weatherData: WeatherResponse)

    @Query("DELETE FROM weather_data WHERE isFav = 0")
    suspend fun deleteFavByIsNotFav()
    @Query("DELETE FROM weather_data WHERE isFav = 1 AND longitude = :longitude AND latitude = :latitude")
    suspend fun deleteFavByLonLat(longitude: Double, latitude: Double)

    @Delete
    suspend fun delete(weatherData: WeatherResponse)




    //retreive all fav cities in the favorite fragment
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM weather_data WHERE isFav = 1")
     fun getFav(): Flow<List<WeatherResponse>>


     //retrive only a specific city weather
     @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
     @Query("SELECT * FROM weather_data WHERE isFav = 1 AND longitude = :longitude AND latitude = :latitude")
     fun getSpecificCityData(longitude: Double, latitude: Double): Flow<WeatherResponse>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM weather_data WHERE isFav = 0 AND longitude = :longitude AND latitude = :latitude")
    fun getSpecificCityDataHome(longitude: Double, latitude: Double): Flow<WeatherResponse>

     //=========================================>Alerts

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertALertData(weatherData: OneApiCall)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM datay")
    fun getAlertsData(): Flow<List<AlertData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(favorite: AlertData)
    @Transaction
    suspend fun setAsAlerted(alert: AlertData, longitude: Double, latitude: Double) {
//        alert.isALert = 1
//        alert.lat = latitude
//        alert.lon = longitude
        Log.i("====db set", "setAsFavorite: ")
        insertAlert(alert)
    }

    @Delete
    suspend fun deleteAlertDataAll(weatherData: AlertData)
}