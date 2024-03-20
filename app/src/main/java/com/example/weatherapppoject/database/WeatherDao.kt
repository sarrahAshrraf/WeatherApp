package com.example.weatherapppoject.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    //insert the weather data when the network fetch them
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeatherData(weatherData: WeatherResponse)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM weather_data")
    fun getWeatherData(): Flow<List<WeatherResponse>>

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

    @Delete
    suspend fun deleteFav(weatherData: WeatherResponse)

    @Query("DELETE FROM weather_data WHERE isFav = 1")
    suspend fun deleteFavByIsFav()
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

}