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

class FiveDaysAdapter(private val forecastArray: List<ForeCastData>,private val language :String, private val units: String): RecyclerView.Adapter<FiveDaysAdapter.ViewHolder>() {

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
            Utils.getWeatherIcon(imageIcon,holder.binding.weatherImgView)

            if(language=="ar"){
                tvTimeRec.text = Utils.convertToArabicNumber(Utils.getTime(currentItem.dt_txt).toString())

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
                tvTimeRec.text = Utils.getTime(currentItem.dt_txt)

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
//                currentItem.main.temp.toString()+"°C"
            Log.i("====RECy", "onBindViewHolder: "+currentItem.weather[0].description)


        }
        }


    override fun getItemCount(): Int {
        return forecastArray.size
    }

}