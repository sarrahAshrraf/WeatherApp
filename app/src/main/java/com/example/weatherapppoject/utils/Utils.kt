package com.example.weatherapppoject.utils

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.weatherapppoject.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Utils {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/"
        const val APIKEY = "3f2c5a9a086fa7d7056043da97b35aae"
        const val MAPS_KEY ="AIzaSyATC4Zk0_xofsFUTm0GRIyNej3syHx5oro"
        const val IMG_URL = "https://openweathermap.org/img/w/"
        @RequiresApi(Build.VERSION_CODES.O)
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        @RequiresApi(Build.VERSION_CODES.O)
        fun getDate(dtTxt: String): LocalDate {
            val dateTime = LocalDateTime.parse(dtTxt, dateFormatter)
            return dateTime.toLocalDate()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getDatefortvDate(dtTxt: String): String {
            val dateTime = LocalDateTime.parse(dtTxt, dateFormatter)
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
            return dateTime.format(dateFormatter)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        public fun getDateAndTime(dtTxt: String): CharSequence {
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val output = DateTimeFormatter.ofPattern("dd-MMMM  HH:mm")
            val dateTime = LocalDateTime.parse(dtTxt, input)
            return output.format(dateTime)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        public fun getTime(dtTxt: String): CharSequence {
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val output = DateTimeFormatter.ofPattern("HH:mm")
            val dateTime = LocalDateTime.parse(dtTxt, input)
            return output.format(dateTime)
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

        fun convertToMeterPerSec(speed: Double): Double {
            return speed / 2.237
        }

        fun convertToMilePerHour(speed: Double): Double {
            return speed * 2.237

        }

        fun getTempSymbol(unit : String) :String{
            return when(unit){
                "metric" -> "C"
                "standard" -> "K"
                else -> "F"
            }
        }

        @SuppressLint("ResourceType")
        fun getWeatherIcon(iconId: String, animationView: LottieAnimationView) {
            val animationResId =
                when (iconId) {
                    "01d" -> R.raw.clearsunnyy
                    "01n" -> R.raw.clearnight
                    "02d" -> R.raw.sunnywithclouds
                    "02n" -> R.raw.cloudynight
                    "03d","03n"-> R.raw.cloudywithwind// clod wa7da sada
                    "04d","04n"-> R.raw.cloudswhitandgray// etnen cloud wa7da soda wa7da byda
                    "09d","09n" -> R.raw.basicrain
                    "10d" -> R.raw.sunnyrain//? clod + sunny+ rain
                    "10n" -> R.raw.nightrain //=>dark cloud night +rain
                    "11d" -> R.raw.sunnythunder
                    "11n" -> R.raw.thunder
                    "13d" -> R.raw.sunnysnow
                    "13n" -> R.raw.nightsnow
                    //Todo update
                    else -> R.raw.windyclouds
                }

            animationView.setAnimation(animationResId)
            animationView.playAnimation()

        }


        @SuppressLint("ResourceType")
        fun getWeatherIconForRecyclerView(iconId: String, img: ImageView) {
               val drawableRes =
                when (iconId) {
                    "01d" -> R.drawable.clearsunny
                    "01n" -> R.drawable.clearnigth
                    "02d" -> R.drawable.ddsunny
                    "02n" -> R.drawable.darkcloudynightpng
                    "03d", "03n" -> R.drawable.cloudyempty
                    "04d", "04n" -> R.drawable.cloudywithwind
                    "09d" -> R.drawable.ddrainy
                    "09n" -> R.drawable.simplwnightrainy
                    "10d" -> R.drawable.heavyddrainy
                    "10n" -> R.drawable.rainnight
                    "11d" -> R.drawable.thunderstorm
                    "11n" -> R.drawable.nightthunder
                    "13d" -> R.drawable.ddsnow
                    "13n" -> R.drawable.nnsnow
                    else -> R.drawable.cloudyempty
                }

            img.setImageResource(drawableRes)

        }
    }




        }