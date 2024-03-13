package com.example.weatherapppoject

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Utils {
    companion object{
       const val  BASE_URL ="https://api.openweathermap.org/data/2.5/"
       const val     APIKEY ="3f2c5a9a086fa7d7056043da97b35aae"
        const val IMG_URL = "https://openweathermap.org/img/w/"

        @RequiresApi(Build.VERSION_CODES.O)
        public fun getDateAndTime(dtTxt :String): CharSequence{
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
            val dateTime = LocalDateTime.parse(dtTxt,input)
            return output.format(dateTime)
        }


    }
}