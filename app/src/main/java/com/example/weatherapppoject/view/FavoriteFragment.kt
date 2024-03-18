package com.example.weatherapppoject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room

import com.example.weatherapppoject.R
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Rain
import com.example.weatherapppoject.forecastmodel.Weather
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var floatingActionButton : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingActionButton  = view.findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener{

//            val db = Room.databaseBuilder(requireContext(), AppDataBase::class.java, "weather-db").build()
//            val main = Main(12.0, 3, 55, 7, 22, 20.5, 55.6, 333.0, 2.0)
//            val weatherList = mutableListOf<Weather>(Weather("description", "icon", 1, "main"))
//            val clouds = Clouds(0)
//            val rain = Rain(3.0)
//            val wind = com.example.weatherapppoject.forecastmodel.Wind(10, 180.3,12.2)
//            val dtTxt = "dt_txt_value"
//            val visibility = 1000
//
//            val weatherModel = WeatherModel(
//                id = 0,
//                icon = "icon_value",
//                clouds = clouds,
//                dt_txt = dtTxt,
//                main = main,
//                rain = rain,
//                visibility = visibility,
//                weather = weatherList,
//                wind = wind
//            )
//            CoroutineScope(Dispatchers.IO).launch {
//                // Insert the WeatherModel into the database
//                db.getWeatherDao().insert(weatherModel)
//            }

            replaceFragments(MapsFragment())

        }

    }
    private fun replaceFragments(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}