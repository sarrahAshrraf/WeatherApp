package com.example.weatherapppoject.view

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.databinding.WeekDaysItemBinding
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.utils.Utils
import com.squareup.picasso.Picasso
import java.time.format.TextStyle
import java.util.Locale

//this is the week adapter
class TodayDataAdapter (private val forecastArray: List<ForeCastData>, private val language: String): RecyclerView.Adapter<TodayDataAdapter.ViewHolder>() {

    class ViewHolder(val binding: WeekDaysItemBinding) : RecyclerView.ViewHolder(binding.root){}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayDataAdapter.ViewHolder {
        return ViewHolder(WeekDaysItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: TodayDataAdapter.ViewHolder, position: Int) {
        val currentItem = forecastArray[position]
        holder.binding.apply {
            val imageIcon = currentItem.weather[0].icon
            //           val imageIcon =  currentItem.time[0].symbol.symbolVar
            Utils.getWeatherIcon(imageIcon,holder.binding.imgeViewRec)
            // Get the day of the week from the date
            val date = Utils.getDate(currentItem.dt_txt)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            dayName.text = dayOfWeek
            tvTimeRec.text = Utils.getDateAndTime(currentItem.dt_txt)
//                currentItem.weather[0].description
//            currentItem.main
//                Utils.getDateAndTime(currentItem.dt_txt)
            if(language=="ar"){
            tvTempRec.text =  Utils.convertToArabicNumber(currentItem.main.temp.toString())+""}
            else{
                tvTempRec.text= currentItem.main.temp.toString()
            }
//                currentItem.main.temp.toString()+"°C"
            Log.i("====RECy", "onBindViewHolder: "+currentItem.weather[0].description)


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