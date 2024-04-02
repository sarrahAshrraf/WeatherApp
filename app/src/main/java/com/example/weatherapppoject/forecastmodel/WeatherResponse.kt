package com.example.weatherapppoject.forecastmodel

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapppoject.onecall.model.Alert

//@Entity(tableName = "weather_data", primaryKeys = ["city", "isFav", "isALert"])

@Entity(tableName = "weather_data")
data class WeatherResponse(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")  val id: Int,
    @ColumnInfo(name = "city")  val city: City,
    @ColumnInfo(name = "longitude") var longitude: Double = 0.0,
    @ColumnInfo(name = "latitude") var latitude: Double = 0.0,
    @ColumnInfo(name = "cnt")  val cnt: Int,
    @ColumnInfo(name = "isFav")  var isFav: Int = 0,
    @ColumnInfo(name = "isALert")  var isALert: Int = 0,
    @ColumnInfo(name = "cod")  val cod: String,
    @ColumnInfo(name = "forecastlist")  val list: MutableList<ForeCastData>,
    @ColumnInfo(name = "message") val message: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long? = null
//    @Embedded val alert: Alert // Embed the Alert object as a column

)