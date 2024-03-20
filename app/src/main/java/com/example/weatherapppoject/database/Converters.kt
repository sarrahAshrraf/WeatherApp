package com.example.weatherapppoject.database

import androidx.room.TypeConverter
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Coord
import com.example.weatherapppoject.forecastmodel.ForeCastData
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

    //forecast data from weather response
    @TypeConverter
    fun fromForecastList(forecastList: List<ForeCastData>): String {
        return Gson().toJson(forecastList)
    }

    @TypeConverter
    fun toForecastList(value: String): List<ForeCastData> {
        val listType = object : TypeToken<List<ForeCastData>>() {}.type
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

    //city -> cord -> lat, lon
    @TypeConverter
    fun fromCity(city: City): String {
        return Gson().toJson(city)
    }

    @TypeConverter
    fun toCity(value: String): City {
        return Gson().fromJson(value, City::class.java)
    }
    @TypeConverter
    fun fromCityCord(city: Coord): String {
        return Gson().toJson(city)
    }

    @TypeConverter
    fun toCityCord(value: String): Coord {
        return Gson().fromJson(value, Coord::class.java)
    }

 }