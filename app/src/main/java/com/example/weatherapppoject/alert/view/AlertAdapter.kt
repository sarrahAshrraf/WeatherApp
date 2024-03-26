package com.example.weatherapppoject.alert.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.databinding.AlertRecycleviewLayoutBinding
import java.util.Calendar

class AlertAdapter (private var alertList: List<AlertData>, var onClick: onClickLinsterInterface): RecyclerView.Adapter<AlertAdapter.myViewHolder>() {
    lateinit var context: Context
    lateinit var alertBinding: AlertRecycleviewLayoutBinding
    class myViewHolder(val viewBinding: AlertRecycleviewLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        alertBinding = AlertRecycleviewLayoutBinding.inflate(inflater,parent,false)
        context=parent.context
        return myViewHolder(alertBinding)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        var alertItem = alertList[position]
        val endTime = trigerTime(alertItem.milleDateTo,alertItem.milleTimeTo)
//        if(Calendar.getInstance().timeInMillis > endTime.timeInMillis)
//            onClick.cancleAlarm(alertItem)
        holder.viewBinding.fromTimeCard.text = alertItem.fromTime
        holder.viewBinding.fromDateCard.text = alertItem.fromDate
        holder.viewBinding.toTimeCard.text = alertItem.toTime
        holder.viewBinding.toDateCard.text = alertItem.toDate
        holder.viewBinding.alertCardDeleteBtn.setOnClickListener{
            dialogDeleteConfirmation(alertItem)
        }

    }

    override fun getItemCount(): Int {
        return alertList.size
    }
    fun trigerTime(toDate:Long,toTime:Long): Calendar {
        var testCanlender = Calendar.getInstance()
        testCanlender.timeInMillis = toDate
        val trigerCalender = Calendar.getInstance()
        trigerCalender.set(Calendar.DAY_OF_MONTH,testCanlender.get(Calendar.DAY_OF_MONTH))
        trigerCalender.set(Calendar.MONTH,testCanlender.get(Calendar.MONTH))
        trigerCalender.set(Calendar.YEAR,testCanlender.get(Calendar.YEAR))
        testCanlender.timeInMillis = toTime
        trigerCalender.set(Calendar.HOUR_OF_DAY,testCanlender.get(Calendar.HOUR_OF_DAY))
        trigerCalender.set(Calendar.MINUTE,testCanlender.get(Calendar.MINUTE))
        return trigerCalender
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setList(List: List<AlertData>){
        alertList = List
        notifyDataSetChanged()
    }
    fun dialogDeleteConfirmation(alertItem: AlertData) {


        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Confirmation")
        alertDialogBuilder.setMessage("Are you sure you want to remove this alert?")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            onClick.cancleAlarm(alertItem)
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }
}
