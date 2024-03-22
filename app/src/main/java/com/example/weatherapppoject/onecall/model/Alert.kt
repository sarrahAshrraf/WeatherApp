package com.example.weatherapppoject.onecall.model


data class Alert(
    val description: String ="",
    val end: Int =0,
    val event: String ="",
    val sender_name: String ="",
    val start: Int =0,
//    val tags: List<Any>
)