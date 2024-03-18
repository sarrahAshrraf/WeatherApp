package com.example.weatherapppoject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import com.example.weatherapppoject.forecastmodel.ForeCastData
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
        //insert the weather data when the network fetch them
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherData: ForeCastData)

//
//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT * FROM products_table")
//    fun getAllProducts(): Flow<List<Product>>
}