package com.example.weatherapppoject.alert.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.databinding.AlertDialogBinding
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import java.util.Calendar

//class AlertDialogFragment : DialogFragment() {
//    private lateinit var CHANEL:String
//
//    lateinit var sharedPreferences: SharedPreferences
//    lateinit var latLonSP: SharedPreferences
//    lateinit var edit: SharedPreferences.Editor
//    lateinit var choise:String
//    lateinit var lat:String
//    lateinit var lon:String
//    var requestCode:Long =0
//    var cashDate1: String? = null
//    var cashDate2: String? = null
//    var cashTime1: String? = null
//    var cashTime2: String? = null
//    var cashCalenderFromTime: Long = 0
//    var cashCalenderFromDate: Long = 0
//    var cashCalenderToTime: Long = 0
//    var cashCalenderToDate: Long = 0
//    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        sharedPreferences = context?.getSharedPreferences("choise", Context.MODE_PRIVATE) as SharedPreferences
//        edit = sharedPreferences.edit()
//        choise = sharedPreferences.getString("what","notification") as String
//        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
//        val dialogBinding = AlertDialogBinding.inflate(layoutInflater)
//
//        val dialog = AlertDialog.Builder(requireContext())
//            .setView(dialogBinding.root)
//            .create()
//
//        dialogBinding.fromBtn.setOnClickListener {
//            pickTime(dialogBinding, 1)
//            pickDate(dialogBinding, 1)
//        }
//        dialogBinding.toBtn.setOnClickListener {
//            pickTime(dialogBinding, 2)
//            pickDate(dialogBinding, 2)
//        }
//        dialogBinding.timeCalenderRadioGroup.setOnCheckedChangeListener{group , checkedId ->
//            if(group.checkedRadioButtonId == R.id.notificationRBtn) {
//                edit.putString("what","notification")
//                edit.commit()
//            }
//            else if(group.checkedRadioButtonId== R.id.alarmRBtn) {
//                edit.putString("what","alarm")
//                edit.commit()
//            }
//        }
//
//        dialogBinding.OkBtn.setOnClickListener {
//            if (cashTime1 != null && cashDate1 != null &&
//                cashTime2 != null && cashDate2 != null &&
//                (dialogBinding.notificationRBtn.isChecked ||
//                        dialogBinding.alarmRBtn.isChecked) &&
//                trigerTime(cashCalenderToDate,cashCalenderToTime).timeInMillis >
//                trigerTime(cashCalenderFromDate,cashCalenderFromTime).timeInMillis
//
//            ) {
//                dialog.dismiss()
//                requestCode= (Calendar.getInstance().timeInMillis)-100
//                CHANEL = requestCode.toString()
//                edit.putString("channel",CHANEL)
//                edit.commit()
//                viewModel.insertIntoAlert(
//                    AlertData(
//                        cashTime1!!,
//                        cashDate1!!,
//                        cashTime2!!,
//                        cashDate2!!,
//                        cashCalenderFromTime,
//                        cashCalenderFromDate,
//                        cashCalenderToTime,
//                        cashCalenderToDate,
//                        requestCode
//                    )
//                )
//
//                setAlarm(sharedPreferences.getString("what","notification").toString())
//                cashDate1 = null
//                cashDate2 = null
//                cashTime1 = null
//                cashTime2 = null
//            } else
//                Toast.makeText(
//                    requireContext(),
//                    "not allowed to let any filed empty or to set the end date before start date",
//                    Toast.LENGTH_LONG
//                ).show()
//        }
//
//
//        //open map
//        dialogBinding.openmapbtn.setOnClickListener {
//            sharedPreferencesManager.setMap(SharedKey.MAP.name,"alert")
//            replaceFragments(MapsFragment())
//        }
//
//
//        return dialog
//    }
//}