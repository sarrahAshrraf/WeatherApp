package com.example.weatherapppoject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapppoject.onecall.model.AlertData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall

//Todo update the entities with -> alerts
@Database(entities = [WeatherResponse::class,OneApiCall::class, AlertData::class] ,exportSchema = false, version = 1)

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
                    "utifei"
                ).build()
                INSTANCE = instance
                instance}
        }
    }
}
