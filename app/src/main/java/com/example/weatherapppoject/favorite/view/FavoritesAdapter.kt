package com.example.weatherapppoject.favorite.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.databinding.FavoriteItemBinding
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesAdapter (
    private var forecastArray: List<WeatherResponse>,
    private val onRemoveClick: (WeatherResponse, Int) -> Unit,
    private val onItemClick: (WeatherResponse, Int) -> Unit
): RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager


    class ViewHolder(val binding: FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root){}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder {
            return ViewHolder(FavoriteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        }

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SuspiciousIndentation", "SetTextI18n")
        override fun onBindViewHolder(holder: FavoritesAdapter.ViewHolder, position: Int) {
            val currentItem = forecastArray[position]
            holder.binding.apply {
                val imageIcon = currentItem.list[0].weather[0].icon
                Utils.getWeatherIcon(imageIcon,holder.binding.imgeViewRec)
                feelslike.text = currentItem.list[0].weather[0].description
                dayName.text =currentItem.city.name

                button.setOnClickListener {
                    onRemoveClick(currentItem, holder.adapterPosition)
                }
                cardView.setOnClickListener {
                    onItemClick(currentItem,holder.adapterPosition)
                }

//                tvTempRec.text = currentItem.list[0].main.temp.toString()
                Log.i("====RECy", "onBindViewHolder: "+currentItem.list[0].weather[0].description)


            }
        }

        override fun getItemCount(): Int {
            return forecastArray.size
        }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<WeatherResponse>) {
        forecastArray = newList
        notifyDataSetChanged()
    }

    }