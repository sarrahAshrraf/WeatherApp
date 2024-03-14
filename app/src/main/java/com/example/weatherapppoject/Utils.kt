package com.example.weatherapppoject

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Utils {
    companion object{
       const val  BASE_URL ="https://api.openweathermap.org/data/2.5/"
       const val  APIKEY = "3f2c5a9a086fa7d7056043da97b35aae"
//       "32860e9888c9f07e4c3912d64cab8a03"

        const val IMG_URL = "https://openweathermap.org/img/w/"

        @RequiresApi(Build.VERSION_CODES.O)
        public fun getDateAndTime(dtTxt :String): CharSequence{
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
            val dateTime = LocalDateTime.parse(dtTxt,input)
            return output.format(dateTime)
        }

        //TODO convert from c to klv
        fun celsiusToKelvin(celsius: Double): Double {
            return celsius + 273.15
        }
        fun kelvinToFahrenheit(temp: Double): Double {
            return temp * 9/5 - 459.67
        }

        fun meterPerSecondToMilePerHour(speed: Double): Double {
            return speed * 2.237
        }

        fun convertToArabicNumber(englishNumberInput: String): String {
            val arabicNumbers = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
            val englishNumbers = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            val builder = StringBuilder()
            for (i in englishNumberInput) {
                if (englishNumbers.contains(i)) {
                    builder.append(arabicNumbers[englishNumbers.indexOf(i)])
                } else {
                    builder.append(i)
                }
            }
            return builder.toString()
        }
        fun translateToArabicNumber(number: Int): String {
            val locale = Locale("ar") // Specify the Arabic locale
            val numberFormat = NumberFormat.getInstance(locale)
            return numberFormat.format(number)
        }


    }
}