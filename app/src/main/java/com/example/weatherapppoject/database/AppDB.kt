package com.example.weatherapppoject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.WeatherResponse

//Todo update the entities with -> alerts
@Database(entities = [ForeCastData::class] ,exportSchema = false, version = 1)

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
                    "oo"
                ).build()
                INSTANCE = instance
                instance}
        }
    }
}
