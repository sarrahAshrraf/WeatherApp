package com.example.weatherapppoject.alert

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.databinding.AlertItemBinding

import com.example.weatherapppoject.onecall.model.OneApiCall
import com.example.weatherapppoject.utils.Utils

class AlertsAdapter( private var alertArray: List<OneApiCall>,
private val onRemoveClick: (OneApiCall, Int) -> Unit,
private val onItemClick: (OneApiCall, Int) -> Unit
): RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {

    class ViewHolder(val binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root){}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsAdapter.ViewHolder {
        return ViewHolder(AlertItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: AlertsAdapter.ViewHolder, position: Int) {
        val currentItem = alertArray[position]
        holder.binding.apply {
            val imageIcon = currentItem.current.weather[0].icon
            //           val imageIcon =  currentItem.time[0].symbol.symbolVar
            Utils.getWeatherIcon(imageIcon,holder.binding.imgeViewRec)
            feelslike.text = currentItem.current.weather[0].description
//            dayName.text = currentItem.city.name
            button.setOnClickListener {
                onRemoveClick(currentItem, holder.adapterPosition)
            }
            cardView.setOnClickListener {
                onItemClick(currentItem,holder.adapterPosition)
            }

            ///from the dialogggggggggg ====TODO
//            if (currentItem.alerts?.get(0)?.description ? != null)
            tvEndDate.text = currentItem.alerts?.get(0)?.description?: "Nothing"
//                Utils.getDateAndTime(currentItem.list[0].dt_txt)

//            tvTempRec.text =  Utils.convertToArabicNumber(currentItem.list[0].main.temp.toString())+".س"
//                currentItem.main.temp.toString()+"°C"
//            Log.i("====RECy", "onBindViewHolder: "+currentItem.list[0].weather[0].description)


        }
    }

    override fun getItemCount(): Int {
        return alertArray.size
    }

    fun submitList(newList: List<OneApiCall>) {
        alertArray = newList
        notifyDataSetChanged()
    }

}