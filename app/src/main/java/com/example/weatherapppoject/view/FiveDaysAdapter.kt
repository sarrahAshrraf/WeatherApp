package com.example.weatherapppoject.view

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapppoject.R
import com.example.weatherapppoject.Utils
import com.example.weatherapppoject.databinding.ItemDetailsCardBinding
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FiveDaysAdapter(private val forecastArray: ArrayList<ForeCastData>): RecyclerView.Adapter<FiveDaysAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemDetailsCardBinding ) : RecyclerView.ViewHolder(binding.root){}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiveDaysAdapter.ViewHolder {
        return ViewHolder(ItemDetailsCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: FiveDaysAdapter.ViewHolder, position: Int) {
        val currentItem = forecastArray[position]
        holder.binding.apply {
           val imageIcon = currentItem.weather[0].icon
             val  imgURL = "https://openweathermap.org/img/w/$imageIcon.png"

                Picasso.get().load(imgURL).into(imageViewRec)

            tvTimeRec.text = Utils.getDateAndTime(currentItem.dt_txt)
            tvTempRec.text = currentItem.main.temp.toString()+"°C"


        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getDateAndTime(dtTxt :String): CharSequence?{
//        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//        val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
//        val dateTime = LocalDateTime.parse(dtTxt,input)
//        return output.format(dateTime)
//
//    }
    override fun getItemCount(): Int {
        return forecastArray.size
    }

}