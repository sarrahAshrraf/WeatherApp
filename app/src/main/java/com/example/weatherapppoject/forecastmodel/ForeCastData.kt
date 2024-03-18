package com.example.weatherapppoject.forecastmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "weather_data", primaryKeys = ["longitude", "latitude", "isFav", "isALert"])
data class ForeCastData(
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0,
    @ColumnInfo(name = "latitude")   val latitude: Double = 0.0,
    @ColumnInfo(name = "clouds") val clouds: Clouds,
    @ColumnInfo(name = "dt")  val dt: Int,
    @ColumnInfo(name = "isFav")  var isFav: Int = 0,
    @ColumnInfo(name = "isALert")  var isALert: Int = 0,
    @ColumnInfo(name = "date") val dt_txt: String,
    @ColumnInfo(name = "mainInfo") val main: Main,
    @ColumnInfo(name = "visibility")     val visibility: Int,
    @ColumnInfo(name = "weatherlist") val weather: MutableList<Weather>,
    @ColumnInfo(name = "wind") val wind: Wind,
//    @ColumnInfo(name = "city") val city: City
)




//@Entity(tableName = "weather_data")
//data class ForeCastData(
//    @PrimaryKey(autoGenerate = true) val id : Int,
//    @ColumnInfo(name = "clouds") val clouds: Clouds,
//    val dt: Int,
//    @ColumnInfo(name = "date") val dt_txt: String,
//    @ColumnInfo (name = "mainInfo")val main: Main,
//    val pop: Double,
////    val rain: Rain,
////    val sys: Sys,
//    val visibility: Int,
//    @ColumnInfo(name = "weatherlist") val weather: MutableList<Weather>,
//    @ColumnInfo (name = "wind")val wind: Wind
//
//)

//
//@Entity(tableName = "fav_data")
//data class FavoriteData(
//    @PrimaryKey val long: Double, @PrimaryKey val lat: Double,
//    @ColumnInfo(name = "clouds") val clouds: Clouds,
//    val dt: Int,
//    var isFav: Int=0,
//    @ColumnInfo(name = "date") val dt_txt: String,
//    @ColumnInfo (name = "mainInfo")val main: Main,
//    @ColumnInfo(name = "weatherlist") val weather: MutableList<Weather>,
//    @ColumnInfo (name = "wind")val wind: Wind
//)