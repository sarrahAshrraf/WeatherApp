package com.example.weatherapppoject.view

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.databinding.ItemDetailsCardBinding
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.squareup.picasso.Picasso

class FiveDaysAdapter(private val forecastArray: List<ForeCastData>): RecyclerView.Adapter<FiveDaysAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemDetailsCardBinding ) : RecyclerView.ViewHolder(binding.root){}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiveDaysAdapter.ViewHolder {
        return ViewHolder(ItemDetailsCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: FiveDaysAdapter.ViewHolder, position: Int) {
        val currentItem = forecastArray[position]
        holder.binding.apply {
           val imageIcon = currentItem.weather[0].icon
            //           val imageIcon =  currentItem.time[0].symbol.symbolVar
             val  imgURL = "https://openweathermap.org/img/w/$imageIcon.png"

                Picasso.get().load(imgURL).into(imageViewRec)

            tvTimeRec.text = currentItem.weather[0].description
            currentItem.main
//                Utils.getDateAndTime(currentItem.dt_txt)
            tvTempRec.text =  Utils.convertToArabicNumber(currentItem.main.temp.toString())
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