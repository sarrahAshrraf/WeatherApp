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
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    //insert the weather data when the network fetch them
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeatherData(weatherData: ForeCastData)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM weather_data")
    fun getWeatherData(): Flow<List<ForeCastData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFav(favorite: ForeCastData)
    @Transaction
    suspend fun setAsFavorite(favorite: ForeCastData) {
        favorite.isFav = 1
        Log.i("====db set", "setAsFavorite: ")
        insertFav(favorite)
    }

    @Delete
    suspend fun deleteFav(weatherData: ForeCastData)

    @Query("DELETE FROM weather_data WHERE isFav = 1")
    suspend fun deleteFavByIsFav()

    //retrive all fav cities in the favorite fragment
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM weather_data WHERE isFav = 1")
    fun getFav(): Flow<List<ForeCastData>>

}