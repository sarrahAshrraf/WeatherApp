package com.example.weatherapppoject.alert

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "datay")
data class AlertData(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var fromTime: String?,
    var fromDate: String?,
    var toTime: String?,
    var toDate: String?,
    var milleTimeFrom: Long,
    var milleDateFrom: Long,
    var milleTimeTo: Long,
    var milleDateTo: Long,
    var requestCode:Long


) {
    constructor(
        fromTime: String,
        fromDate: String,
        toTime: String,
        toDate: String,
        milleTimeFrom: Long,
        milleDateFrom: Long,
        milleTimeTo: Long,
        milleDateTo: Long,
        requestCode: Long
    )
            : this(
        null,
        fromTime,
        fromDate,
        toTime,
        toDate,
        milleTimeFrom,
        milleDateFrom,
        milleTimeTo,
        milleDateTo,
        requestCode
    )
}