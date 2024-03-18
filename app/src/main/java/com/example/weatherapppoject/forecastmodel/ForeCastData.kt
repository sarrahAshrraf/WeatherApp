package com.example.weatherapppoject.forecastmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class ForeCastData(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "clouds") val clouds: Clouds,
    val dt: Int,
    @ColumnInfo(name = "date") val dt_txt: String,
    @ColumnInfo (name = "mainInfo")val main: Main,
    val pop: Double,
//    val rain: Rain,
//    val sys: Sys,
    val visibility: Int,
    @ColumnInfo(name = "weatherlist") val weather: MutableList<Weather>,
    @ColumnInfo (name = "wind")val wind: Wind

)