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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Utils {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val APIKEY = "3f2c5a9a086fa7d7056043da97b35aae"
//       "32860e9888c9f07e4c3912d64cab8a03"

        const val IMG_URL = "https://openweathermap.org/img/w/"


        @RequiresApi(Build.VERSION_CODES.O)
        public fun getDateAndTime(dtTxt: String): CharSequence {
            val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
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

        //TODO convert from c to klv
        fun celsiusToKelvin(celsius: Double): Double {
            return celsius + 273.15
        }

        fun kelvinToFahrenheit(temp: Double): Double {
            return temp * 9 / 5 - 459.67
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

        @SuppressLint("ResourceType")
        fun getWeatherIcon(iconId: String, animationView: LottieAnimationView) {
            val animationResId =
                when (iconId) {
                    "01d" -> R.raw.clearsunny
                    "01n" -> R.raw.clearnight
                    "02d" -> R.raw.sunnywithclouds
                    "02n" -> R.raw.cloudynight
                    "03d", "03n" -> R.raw.cloudywithwind
                    "04d", "04n" -> R.raw.cloudywithwind
                    "09d" -> R.raw.rain
                    "09n" -> R.raw.rain
                    "10d" -> R.raw.sunnyrain
                    "10n" -> R.raw.nightrain
                    "11d" -> R.raw.sunnythunder
                    "11n" -> R.raw.thunder
                    "13d" -> R.raw.sunnysnow
                    "13n" -> R.raw.nightsnow
                    //Todo update
                    else -> R.raw.cloudywithwind
                }

            animationView.setAnimation(animationResId)
            animationView.playAnimation()

        }


//        @SuppressLint("ResourceType")
//        fun getWeatherIconBackground(iconId: String, animationView: LottieAnimationView) {
//            val animationResId =
//                when (iconId) {
//                    "09d" -> R.raw.rainbackground
//                    "09n" -> R.raw.rainbackground
//                    "10d" -> R.raw.rainbackground
//                    "10n" -> R.raw.rainbackground
//                    else -> null
//                }
//
//            if (animationResId != null) {
//                animationView.setAnimation(animationResId)
//            }
//            animationView.playAnimation()
//
//        }


    }

        }









//
//val iconId = weatherList!!.weather[0].icon
//val imgURL = "https://openweathermap.org/img/w/$iconId.png"
//
//when (iconId) {
//    "01d" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.ddsunny)
//            .into(binding.weatherImgView)
//    }
//    "01n" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.sunny)
//            .into(binding.weatherImgView)
//    }
//    "02d" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.partlycloudy)
//            .into(binding.weatherImgView)
//    }
//    "02n" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.cloudynight)
//            .into(binding.weatherImgView)
//    }
//    "03d", "03n", "04d", "04n" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.cloudy)
//            .into(binding.weatherImgView)
//    }
//    "09d", "09n" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.ddrainy)
//            .into(binding.weatherImgView)
//    }
//    "10d", "10n" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.rainy)
//            .into(binding.weatherImgView)
//    }
//    "11d", "11n" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.thunderstorm)
//            .into(binding.weatherImgView)
//    }
//    "13d", "13n" -> {
//        Glide.with(requireContext())
//            .load(R.drawable.snow)
//            .into(binding.weatherImgView)
//    }
//    "50d", "50n" -> {
//        Glide.with(requireContext())
////                        .load(R.drawable.mist)
////                        .into(binding.weatherImgView)
//    }
//    else -> {
//        Glide.with(requireContext())
//            .load(imgURL)
//            .into(binding.weatherImgView)
//    }
//}
//
//
//
//
//}
