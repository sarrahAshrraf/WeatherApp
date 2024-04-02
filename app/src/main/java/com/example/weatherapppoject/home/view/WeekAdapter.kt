package com.example.weatherapppoject.home.view

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.databinding.WeekDaysItemBinding
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.utils.Utils
import java.time.format.TextStyle
import java.util.Locale

//this is the week adapter
class WeekAdapter (private val forecastArray: List<ForeCastData>, private val language: String, private val units: String): RecyclerView.Adapter<WeekAdapter.ViewHolder>() {
    class ViewHolder(val binding: WeekDaysItemBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(WeekDaysItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = forecastArray[position]
        holder.binding.apply {
            val imageIcon = currentItem.weather[0].icon
            Utils.getWeatherIconForRecyclerView(imageIcon,holder.binding.imgeViewRec)
            val date = Utils.getDate(currentItem.dt_txt)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            dayName.text = dayOfWeek
            tvTimeRec.text = Utils.getDatefortvDate(currentItem.dt_txt)
            if(language=="ar"){
                tvTimeRec.text = Utils.convertToArabicNumber(Utils.getDate(currentItem.dt_txt).toString())

                if(units=="metric"){
                    tvTempRec.text ="م° " +Utils.convertToArabicNumber(currentItem.main.temp.toString())

                }
                else if(units=="imperial"){
                    tvTempRec.text = " ف° "+ Utils.convertToArabicNumber(currentItem.main.temp.toString())

                }
                else {
                    tvTempRec.text =  Utils.convertToArabicNumber(currentItem.main.temp.toString())+"ك° "

                }
            }
            //english
            else{
                if(units=="metric"){
                    tvTempRec.text = currentItem.main.temp.toString()+"°C"

                }
                else if(units=="imperial"){
                    tvTempRec.text = currentItem.main.temp.toString()+"°F"

                }
                else {
                    tvTempRec.text= currentItem.main.temp.toString()+"°K"

                }
            }

        }
    }
    override fun getItemCount(): Int {
        return forecastArray.size
    }

}