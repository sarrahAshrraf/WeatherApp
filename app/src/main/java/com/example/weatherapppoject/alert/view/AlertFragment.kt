package com.example.weatherapppoject.alert.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.alert.NetworkListener
import com.example.weatherapppoject.alert.viewmodel.AlertViewModel
import com.example.weatherapppoject.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.AlertDialogBinding
import com.example.weatherapppoject.databinding.FragmentAlertBinding
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.view.HomeActivity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class AlertFragment : Fragment(),onClickLinsterInterface {
    private lateinit var channelCode:String
    lateinit var binding: FragmentAlertBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pIntent: PendingIntent
    lateinit var alertFactory: AlertViewModelFactory
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    lateinit var viewModel: AlertViewModel
    lateinit var alertAdapter: AlertAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var sharedPreferences: SharedPreferences
    lateinit var edit: SharedPreferences.Editor
    lateinit var choice:String
    lateinit var lat:String
    lateinit var lon:String
    var requestCode:Long =0
    var dateOneSelected: String? = null
    var dateTwoSelected: String? = null
    var timeOne: String? = null
    var timeTwo: String? = null
    var calenderFromTime: Long = 0
    var calenderFromDate: Long = 0
    var calenderToTime: Long = 0
    var calenderToDate: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Alerts")
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        sharedPreferences = context?.getSharedPreferences("choice", Context.MODE_PRIVATE) as SharedPreferences
        edit = sharedPreferences.edit()
        choice = sharedPreferences.getString(SharedKey.ALERT_TYPE.name,"notification") as String
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())

        if(sharedPreferencesManager.getSavedMap(SharedKey.MAP.name,"")=="alert") {
            val longlat = sharedPreferencesManager.getLocationToAlert(SharedKey.ALERT.name)
            val longg = longlat!!.first
            val latt = longlat.second
            lat =latt.toString()
            lon = longg.toString()
            Log.i("=====latlong Alert", ": "+lat + " "+ lon)


        }
        else { //map is home
            val alertLongLat = sharedPreferencesManager.getLocationToAlert(SharedKey.ALERT.name)
            val latC = alertLongLat!!.first
            val lonC = alertLongLat!!.second
            lat=latC.toString()
            lon =lonC.toString()

            Log.i("======aaajksl", "else in alert location "+lat)



        }

        binding.radioSelection.setOnCheckedChangeListener { radioGroup, checkedId ->
//           binding.linearLayout.setOnClickListener {
               val checkedRadioButton = radioGroup.findViewById<RadioButton>(checkedId)

               if (!checkedRadioButton.isChecked) {
//                checkedRadioButton.isChecked = true
               } else {
                   when (checkedId) {
                       R.id.radioCurrent -> {
                           binding.radioMap.isChecked = false
                           lon= 30.006545148789886.toString()
                           lat = 31.243691112649426.toString()
                           Toast.makeText(
                               requireContext(),
                               "Current location has been saved",
                               Toast.LENGTH_SHORT
                           ).show()

                           sharedPreferencesManager.setMap(SharedKey.MAP.name, "home")
                       }

                       R.id.radioMap -> {
                        binding.radioCurrent.isChecked = false

                           Toast.makeText(
                               requireContext(),
                               "Map location has been selected",
                               Toast.LENGTH_SHORT
                           ).show()

                           sharedPreferencesManager.setMap(SharedKey.MAP.name, "alert")
                           replaceFragments(MapsFragment())

                   }
               }
           }
        }

//        binding.btnMapALert.setOnClickListener {
//
//            if (NetworkListener.getConnectivity(requireContext())) {
//            sharedPreferencesManager.setMap(SharedKey.MAP.name,"alert")
//            replaceFragments(MapsFragment())}
//            else {
//                Snackbar.make(
//                    binding.btnMapALert,
//                    "There is no internet connection",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }
//        binding.btnLocationAlert.setOnClickListener{
//
//            sharedPreferencesManager.setMap(SharedKey.MAP.name, "home")
//            Toast.makeText(
//                               requireContext(),
//                               "Current location has been saved",
//                               Toast.LENGTH_SHORT
//                           ).show()
//        }



        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository= WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)

        alertFactory = AlertViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), alertFactory).get(AlertViewModel::class.java)
        alertAdapter= AlertAdapter(listOf(),this)
        linearLayoutManager = LinearLayoutManager(context)
//        binding.swipeRefresh.setOnRefreshListener {
//            observeAtData()
//            binding.swipeRefresh.isRefreshing =false
//        }
        UpdateData()
        binding.alertRV.adapter = alertAdapter
        binding.alertRV.layoutManager = linearLayoutManager

        binding.btnCompleteAction.setOnClickListener {
            if (NetworkListener.getConnectivity(requireContext())) {
                if (!Settings.canDrawOverlays(requireContext())){
                    requestOverAppPermission()
                }
                showDialog()
            } else
                //TODO + don't open the maaaaap bardooooooo
                Snackbar.make(
                    binding.btnCompleteAction,
                    "There is no internet connection",
                    Snackbar.LENGTH_LONG
                ).show()
        }
        return binding.root
    }
    fun UpdateData(){
        viewModel.alertData.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                binding.lottieLayerName.visibility = View.GONE
            }else
                binding.lottieLayerName.visibility = View.VISIBLE
            alertAdapter.setList(data)
        }

    }
    fun showDialog() {
        val dialogBinding = AlertDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
//        dialogBinding.tvLat.text = lat
        dialogBinding.fromBtn.setOnClickListener {
            pickTime(dialogBinding, 1)
            pickDate(dialogBinding, 1)
        }
        dialogBinding.toBtn.setOnClickListener {
            pickTime(dialogBinding, 2)
            pickDate(dialogBinding, 2)
        }
        dialogBinding.timeCalenderRadioGroup.setOnCheckedChangeListener{group , checkedId ->
            if(group.checkedRadioButtonId == R.id.notificationRBtn) {
                edit.putString(SharedKey.ALERT_TYPE.name,"notification")
                edit.commit()
            }
            else if(group.checkedRadioButtonId==R.id.alarmRBtn) {
                edit.putString(SharedKey.ALERT_TYPE.name,"alarm")
                edit.commit()
            }
        }

        dialogBinding.OkBtn.setOnClickListener {
            if (timeOne != null && dateOneSelected != null &&
                timeTwo != null && dateTwoSelected != null &&
                (dialogBinding.notificationRBtn.isChecked ||
                        dialogBinding.alarmRBtn.isChecked) &&
                trigerTime(calenderToDate,calenderToTime).timeInMillis >
                trigerTime(calenderFromDate,calenderFromTime).timeInMillis

            ) {
                dialog.dismiss()
                requestCode= (Calendar.getInstance().timeInMillis)-100
                channelCode = requestCode.toString()
                edit.putString("channel",channelCode)
                edit.commit()
                viewModel.insertIntoAlert(
                    AlertData(
                        timeOne!!,
                        dateOneSelected!!,
                        timeTwo!!,
                        dateTwoSelected!!,
                        calenderFromTime,
                        calenderFromDate,
                        calenderToTime,
                        calenderToDate,
                        requestCode,
                        lon, lat
                    )
                )

                setAlarm(sharedPreferences.getString(SharedKey.ALERT_TYPE.name,"notification").toString())
                dateOneSelected = null
                dateTwoSelected = null
                timeOne = null
                timeTwo = null
            } else
                Toast.makeText(
                    requireContext(),
                    "Warning! please complete all fields and don't set the end date before start date",
                    Toast.LENGTH_LONG
                ).show()
        }


//        //open map
//        dialogBinding.openmapbtn.setOnClickListener {
//            sharedPreferencesManager.setMap(SharedKey.MAP.name,"alert")
//            replaceFragments(MapsFragment())
//        }
        dialog.show()
    }

    private fun replaceFragments(fragment: Fragment) {
        val transaction = (context as HomeActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    // picker time and picker date
    private fun pickTime(dialogBinding: AlertDialogBinding, choose: Int) {
        val calendarTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
            calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarTime.set(Calendar.MINUTE, minute)
            calendarTime.timeZone = TimeZone.getDefault()
            updateTimeLabel(calendarTime, dialogBinding, choose)
        }
        TimePickerDialog(
            requireContext(),
            timePicker,
            calendarTime.get(Calendar.HOUR_OF_DAY),
            calendarTime.get(Calendar.MINUTE),
            false
        ).show()
    }
    private fun pickDate(dialogBinding: AlertDialogBinding, choose: Int){
        val calenderDate = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calenderDate.set(Calendar.YEAR, year)
            calenderDate.set(Calendar.MONTH, month)
            calenderDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel(calenderDate, dialogBinding, choose)
        }
        DatePickerDialog(
            requireContext(),
            datePicker,
            calenderDate.get(Calendar.YEAR),
            calenderDate.get(Calendar.MONTH),
            calenderDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTimeLabel(
        calendarTime: Calendar,
        dialogBinding: AlertDialogBinding,
        choose: Int
    ) {
        val format = SimpleDateFormat("hh:mm:aa")
        val time = format.format(calendarTime.time)
        when (choose) {
            1 -> {
                dialogBinding.fromTime.text = time
                timeOne = time
                calenderFromTime = calendarTime.timeInMillis
            }
            2 -> {
                dialogBinding.toTime.text = time
                timeTwo = time
                calenderToTime = calendarTime.timeInMillis
            }

        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun updateDateLabel(
        calenderDate: Calendar,
        dialogBinding: AlertDialogBinding,
        choose: Int
    ) {
        val day = SimpleDateFormat("dd").format(calenderDate.time)
        val month = SimpleDateFormat("MM").format(calenderDate.time)
        val year = SimpleDateFormat("yyyy").format(calenderDate.time)
        when (choose) {
            1 -> {
                dialogBinding.fromCalender.text = "${day}/${month}/${year}"
                dateOneSelected = "${day}/${month}/${year}"
                calenderFromDate = calenderDate.timeInMillis
            }
            2 -> {
                dialogBinding.toCalender.text = "${day}/${month}/${year}"
                dateTwoSelected = "${day}/${month}/${year}"
                calenderToDate = calenderDate.timeInMillis
            }
        }
    }

    fun deleteAlert(alertData: AlertData) {
        viewModel.deleteFromAlert(alertData)

    }

    // set Alarm
    private fun setAlarm(type:String) {
        var noOfDays = calenderToDate - calenderFromDate
        val days = TimeUnit.MILLISECONDS.toDays(noOfDays)
        val dayInMilliSecond = 24 * 60 * 60 * 1000
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("channel",channelCode)
        intent.putExtra(SharedKey.ALERT_TYPE.name,type)
        intent.putExtra("lat",lat)
        intent.putExtra("lon",lon)

        for (i in 0..days) {
            createNotificationChannel()
            pIntent =
                PendingIntent.getBroadcast(requireContext(), (requestCode + i).toInt(), intent,
                    PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP, trigerTime(calenderFromDate,calenderFromTime).timeInMillis + (i * dayInMilliSecond), pIntent
            )
        }
    }


    override fun cancleAlarm(alertData: AlertData) {
        var noOfDays = alertData.milleDateTo - alertData.milleDateFrom
        val days = TimeUnit.MILLISECONDS.toDays(noOfDays)
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmReceiver::class.java)
        Log.i("days ", "cancleAlarm: "+days)
        for (i in 0..days) {
            pIntent =
                PendingIntent.getBroadcast(context, (alertData.requestCode + i).toInt(), intent,
                    PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pIntent)
        }
        deleteAlert(alertData)
    }



    fun trigerTime(Date:Long,Time:Long): Calendar {
        var testCanlender = Calendar.getInstance()
        testCanlender.timeInMillis = Date
        val trigerCalender = Calendar.getInstance()
        trigerCalender.set(Calendar.DAY_OF_MONTH,testCanlender.get(Calendar.DAY_OF_MONTH))
        trigerCalender.set(Calendar.MONTH,testCanlender.get(Calendar.MONTH))
        trigerCalender.set(Calendar.YEAR,testCanlender.get(Calendar.YEAR))
        testCanlender.timeInMillis = Time
        trigerCalender.set(Calendar.HOUR_OF_DAY,testCanlender.get(Calendar.HOUR_OF_DAY))
        trigerCalender.set(Calendar.MINUTE,testCanlender.get(Calendar.MINUTE))
        return trigerCalender
    }
    //create notification
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel display name"
            val description = "Channel from alarm manager"
            var importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelCode, name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


    private fun requestOverAppPermission() {
        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),20)
    }
}