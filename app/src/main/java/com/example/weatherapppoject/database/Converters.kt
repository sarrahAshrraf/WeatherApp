package com.example.weatherapppoject.database

import androidx.room.TypeConverter
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Weather
import com.example.weatherapppoject.forecastmodel.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    //weather list -> description + icon
    @TypeConverter
    fun fromWeatherList(weatherList: List<Weather>): String {
        return Gson().toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    //Main -> humidity , temp, pressure
    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(value: String): Main {
        return Gson().fromJson(value, Main::class.java)
    }

    //clouds -> all
    @TypeConverter
    fun fromClouds(clouds: Clouds): Int {
        return clouds.all
    }

    @TypeConverter
    fun toClouds(value: Int): Clouds {
        return Clouds(value)
    }

    //wind -> speed
    @TypeConverter
    fun fromWind(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(value: String): Wind {
        return Gson().fromJson(value, Wind::class.java)
    }

 }