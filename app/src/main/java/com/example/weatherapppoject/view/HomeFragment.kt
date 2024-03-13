package com.example.weatherapppoject.view

import android.content.res.Configuration
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import com.bumptech.glide.Glide
import com.example.weatherapppoject.R
import com.example.weatherapppoject.Utils
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.databinding.FragmentSettingsBinding
import com.example.weatherapppoject.network.RetrofitInstance
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locale = Locale(sharedPreferencesManager.getString(SharedKey.LANGUAGE.name, "default"))
        Locale.setDefault(locale)
        val resources = requireContext().resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        val context= ContextUtils.wrapContext(requireContext(), locale)
        getCurrentWeather()

        binding.city.setText(context.getString(R.string.city))
        binding.textDays.setText(context.getString(R.string.language))
        binding.tvPressure.setText(context.getString(R.string.pressure))
        binding.textDays.setText(context.getString(R.string.next5days))
        binding.tvTemp.setText(context.getString(R.string.weathertemp))
        binding.tvHumidity.setText(context.getString(R.string.humidity))
        binding.tvclouds.setText(context.getString(R.string.clouds))
        binding.tvDayFormat.setText(context.getString(R.string.dayformat))
        binding.tvStatus.setText(context.getString(R.string.weatherstatus))
    }
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
private fun getCurrentWeather(){
    GlobalScope.launch (Dispatchers.IO){
        val response = try {
            RetrofitInstance.wetherAPi.getCureentWeather("London","metric",Utils.APIKEY)
        } catch (e: IOException){
        Toast.makeText(requireContext(),"error"+e,Toast.LENGTH_SHORT).show()
            Log.i("============", "Error"+e)
        return@launch
    } catch (e :HttpException){
            Toast.makeText(requireContext(),""+e,Toast.LENGTH_SHORT).show()
            Log.i("============", "Error"+e)

            return@launch


        }

    if(response.isSuccessful && response.body()!=null){
        Log.i("===========", "city: "+response.body()!!.clouds.toString())
        Log.i("===========", "city: "+response.body()!!.main?.temp)

        withContext(Dispatchers.Main){
           val data = response.body()
//            binding.tvDayFormat.text= SimpleDateFormat(
//                "hh:mm a",
//                Locale.ENGLISH
//            ).format(data.sys.)
           binding.apply {
               tvTemp.text = response.body()!!.main?.temp.toString()
               cloudPercent.text= response.body()!!.clouds.toString()
               tvWind.text = response.body()!!.wind.toString()
               tvDayFormat.text = response.body()!!.dtTxt
               humidityPercent.text = response.body()!!.main?.humidity.toString()
               pressurePercent.text = response.body()!!.main?.pressure.toString()
               tvStatus.text = response.body()!!.weather[0].description




           }
/*
 icons : day d + night n
 01 - sunny
 02 - sun +cloud
 03 - one cloud
 04 - clouds
 09 - rains
*/

            val iconId = data!!.weather[0].icon
            val imgURL = "https://openweathermap.org/img/w/$iconId.png"

            Log.i("===========icin", "getCurrentWeather: "+data.weather[0].icon)

            when (iconId) {
                "01d" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.ddsunny)
                        .into(binding.weatherImgView)
                }
                "01n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.sunny)
                        .into(binding.weatherImgView)
                }
                "02d" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.partlycloudy)
                        .into(binding.weatherImgView)
                }
                "02n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.cloudynight)
                        .into(binding.weatherImgView)
                }
                "03d", "03n", "04d", "04n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.cloudy)
                        .into(binding.weatherImgView)
                }
                "09d", "09n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.ddrainy)
                        .into(binding.weatherImgView)
                }
                "10d", "10n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.rainy)
                        .into(binding.weatherImgView)
                }
                "11d", "11n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.thunderstorm)
                        .into(binding.weatherImgView)
                }
                "13d", "13n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.snow)
                        .into(binding.weatherImgView)
                }
                "50d", "50n" -> {
                    Glide.with(requireContext())
//                        .load(R.drawable.mist)
//                        .into(binding.weatherImgView)
                }
                else -> {
                    Glide.with(requireContext())
                        .load(imgURL)
                        .into(binding.weatherImgView)
                }
            }
//
//            if (iconId =="04n"){
//                val imgURL = "https://openweathermap.org/img/w/$iconId.png"
//                Glide.with(requireContext())
//                    .load(R.drawable.cloudy)
//                    .into(binding.weatherImgView)
//            }

        }
    }
    }
}
}
