package com.example.weatherapppoject.alert.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.databinding.AlertDetailsItemBinding
import java.util.Calendar

class AlertAdapter (private var alertList: List<AlertData>, var onClick: onClickLinsterInterface): RecyclerView.Adapter<AlertAdapter.myViewHolder>() {
    lateinit var context: Context
    lateinit var alertBinding: AlertDetailsItemBinding
    class myViewHolder(val viewBinding: AlertDetailsItemBinding): RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        alertBinding = AlertDetailsItemBinding.inflate(inflater,parent,false)
        context=parent.context
        return myViewHolder(alertBinding)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        var alertItem = alertList[position]
        val endTime = choosenTime(alertItem.milleDateTo,alertItem.milleTimeTo)
        if(Calendar.getInstance().timeInMillis > endTime.timeInMillis)
            onClick.onAlarmCLick(alertItem)
        holder.viewBinding.timeStartTxt.text = alertItem.fromTime
        holder.viewBinding.startDateText.text = alertItem.fromDate
        holder.viewBinding.timeEndTxt.text = alertItem.toTime
        holder.viewBinding.endDateText.text = alertItem.toDate
        holder.viewBinding.button.setOnClickListener{
            dialogDeleteConfirmation(alertItem)
        }

    }

    override fun getItemCount(): Int {
        return alertList.size
    }
    fun choosenTime(toDate:Long, toTime:Long): Calendar {
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
        alertDialogBuilder.setTitle(R.string.removeDialogmsg)
        alertDialogBuilder.setMessage(R.string.deletAlertConfirm)
        alertDialogBuilder.setPositiveButton(R.string.ok) { dialog, _ ->
            onClick.onAlarmCLick(alertItem)
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }
}
