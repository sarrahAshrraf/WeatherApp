package com.example.weatherapppoject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall

//Todo update the entities with -> alerts
@Database(entities = [WeatherResponse::class,OneApiCall::class] ,exportSchema = false, version = 1)

@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {

    abstract fun getWeatherDAO(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null
        @Synchronized
        fun getInstance(context: Context): AppDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDB::class.java,
                    "yal"
                ).build()
                INSTANCE = instance
                instance}
        }
    }
}
