package com.example.weatherapppoject.onecall.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "alert_data", primaryKeys = ["lat","lon", "isALert"])
data class OneApiCall(
//    @ColumnInfo(name = "alerts")  val alerts: List<Alert> = emptyList(),
    @ColumnInfo(name = "alerts") val alerts: List<Alert>? = null,

    @ColumnInfo(name = "current")  val current: Current,
//    @ColumnInfo(name = "isFav")  var isFav: Int = 0,
    @ColumnInfo(name = "isALert")  var isALert: Int = 0,
//    val daily: List<Daily>,
//    val hourly: List<Hourly>,
    @ColumnInfo(name = "lat")  var lat: Double,
    @ColumnInfo(name = "lon") var lon: Double,
//    @ColumnInfo(name = "minutely") val minutely: List<Minutely>,
    @ColumnInfo(name = "timezone") val timezone: String,
    @ColumnInfo(name = "timezone_offset") val timezone_offset: Int
)