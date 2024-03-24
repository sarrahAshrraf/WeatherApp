package com.example.weatherapppoject.alerts

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alerts.service.AlarmReciver
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentNotificationBinding
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ALertDBState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch




class NotificationFragment : Fragment() {
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
//    private lateinit var alertViewModel: AlertViewModel
    private lateinit var repository: WeatherRepositoryImpl
//    private lateinit var alertViewModelFactory: AlertViewModelFactory
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    private lateinit var alertsAdapter: AlertsAdapter
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var alertLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentNotificationBinding
    private var datePickerDialog: DatePickerDialog?= null
    private var timePicker: TimePickerDialog?= null
    var forgroundServiceIntent: Intent?=null

    private lateinit var picker : MaterialTimePicker
    private lateinit var calendar : Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource, localDataSourceInte)

//        alertViewModelFactory = AlertViewModelFactory(repository)
//        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
        alertLayoutManager = LinearLayoutManager(requireContext())
        forgroundServiceIntent = Intent(requireContext(),AlarmReciver::class.java)
        //foreground service class

    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alertsRecView.layoutManager = alertLayoutManager

//        alertsAdapter = AlertsAdapter(emptyList(),
//            { product, position ->
//                // Delete confirmation dialog
                val alertDialogBuilder = AlertDialog.Builder(context)
//                alertDialogBuilder.setTitle("Delete Confirmation")
//                alertDialogBuilder.setMessage("Are you sure you want to remove this item?")
//                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
//                    alertViewModel.removeFromALerts(product)
//                    dialog.dismiss()
//                }
//                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.dismiss()
//                }
//                val alertDialog = alertDialogBuilder.create()
//                alertDialog.show()
//            },
//            { product, position ->
//                // On card click listener
//                Toast.makeText(requireContext(), "on card", Toast.LENGTH_SHORT).show()
//            }
//        )
//
//        binding.alertsRecView.adapter = alertsAdapter
//        alertViewModel.showAlertsItems()
//        lifecycleScope.launch(Dispatchers.Main) {
//
//            alertViewModel.AlertData.collect { state ->
//                when (state) {
//                    is ALertDBState.Loading -> {
//                        Toast.makeText(requireContext(), "loadi ", Toast.LENGTH_SHORT).show()
//
//                    }
//
//                    is ALertDBState.Suceess -> {
//                        val alertData = state.alertdata
//                        alertsAdapter.submitList(alertData)
//                        if (alertData.isEmpty()) {
//                            binding.animationView.visibility = View.VISIBLE
//                            binding.AddAlertfloatingActionButton.visibility = View.GONE
//                        } else {
//                            binding.animationView.visibility = View.GONE
//                        }
//                    }
//
//                    else -> {
//                        Toast.makeText(requireContext(), "else ", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }


//        binding.AddAlertfloatingActionButton.setOnClickListener {
//            val alertDialogBuilder = AlertDialog.Builder(requireContext())
//            alertDialogBuilder.setTitle("Alert Setup")
//
//            val dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)
//            alertDialogBuilder.setView(dialogView)
//            val alertDialog = alertDialogBuilder.create()
//
//            val tvStart = dialogView.findViewById<TextView>(R.id.tv_start_date)
//            val tvEnd = dialogView.findViewById<TextView>(R.id.tv_end_date)
//            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelAlert)
//            val btnConfirm = dialogView.findViewById<Button>(R.id.btnSaveAlert)
//            tvStart.setOnClickListener {
//                openDatepicker()
//            }
//            btnCancel.setOnClickListener {
//                alertDialog.dismiss()
//            }
//            btnConfirm.setOnClickListener {
////             TODO   saveData(txt,txt)
//            }
//
//            alertDialog.show()
//
//        }

    }

private fun openDatepicker(){
    val cldr = Calendar.getInstance()
    val day= cldr[Calendar.DAY_OF_MONTH]
    val month = cldr[Calendar.MONTH]
    val year = cldr[Calendar.YEAR]
    datePickerDialog = DatePickerDialog(
        requireContext(),
        { view, year, monthOfYear, dayOfMonth ->
            val month = monthOfYear + 1
            var strMonth = "" + month
            var strDayOfMonth = "" + dayOfMonth
            if (month < 10) {
                strMonth = "0$strMonth"
            }
            if (dayOfMonth < 10) {
                strDayOfMonth = "0$strDayOfMonth"
            }
            val date = "$strDayOfMonth / $strMonth /$year"
            Log.i("======Date" ,"openDatepicker: "+date)
        } , year, month, day
    )
    datePickerDialog!!.setTitle("select date")
    datePickerDialog!!.setButton(DialogInterface.BUTTON_POSITIVE, "Save") { _, _ ->
        openTimepicker()
    }
    datePickerDialog!!.show()
//    datePickerDialog.po
}



    private fun openTimepicker(){
        val selectedTime = Calendar.getInstance()
        val hour= selectedTime[Calendar.HOUR_OF_DAY]
        val minute = selectedTime[Calendar.MINUTE]

        timePicker = TimePickerDialog(
            requireContext(),
            { view, hourOfDay, minuteOfHour ->
                var hour = "" + hourOfDay
                var minute = "" + minuteOfHour
                if (hourOfDay < 10) {
                    hour = "0$hour"
                }
                if (minuteOfHour < 10) {
                    minute = "0$minute"
                }
                val time = "$hour : $minute"
                Log.i("======Date" ,"openDatepicker: "+time)
            } , hour, minute, false
        )
        timePicker!!.setTitle("select date")
        timePicker!!.show()

    }



}



//        binding.AddAlertfloatingActionButton.setOnClickListener {
//            val alertDialogBuilder = AlertDialog.Builder(requireContext())
//            alertDialogBuilder.setTitle("Alert Setup")
//
//            // Create a custom layout for the AlertDialog
//            val dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)
//            alertDialogBuilder.setView(dialogView)
//
//            // Get references to the views in the custom layout
//            val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
//            val startTimePicker = dialogView.findViewById<TimePicker>(R.id.startTimePicker)
//            val endTimePicker = dialogView.findViewById<TimePicker>(R.id.endTimePicker)
//            val tvStart = dialogView.findViewById<TextView>(R.id.tv_start_date)
//            val tvEnd = dialogView.findViewById<TextView>(R.id.tv_end_date)
//            datePicker.visibility = View.GONE
//            startTimePicker.visibility = View.GONE
//            endTimePicker.visibility = View.GONE
//
//            tvStart.setOnClickListener {
////                datePicker.visibility = View.VISIBLE
//
//                startTimePicker.visibility = View.VISIBLE
//                endTimePicker.visibility = View.GONE
//            }
//
//            tvEnd.setOnClickListener {
//                startTimePicker.visibility = View.GONE
//                endTimePicker.visibility = View.VISIBLE
//            }
//
//            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
//                // Get the selected date from the DatePicker
//                val year = datePicker.year
//                val month = datePicker.month
//                val day = datePicker.dayOfMonth
//
//                // Get the selected start time from the TimePicker
//                val startHour = startTimePicker.hour
//                val startMinute = startTimePicker.minute
//
//                // Get the selected end time from the TimePicker
//                val endHour = endTimePicker.hour
//                val endMinute = endTimePicker.minute
//
//                // Perform the necessary actions with the selected date and times
//                // You can access the values using the variables `year`, `month`, `day`, `startHour`, `startMinute`, `endHour`, `endMinute`
//
//                // Format the selected date and times as desired
//                val startDate = String.format("%02d/%02d/%d", day, month + 1, year)
//                val startTime = String.format("%02d:%02d", startHour, startMinute)
//                val endTime = String.format("%02d:%02d", endHour, endMinute)
//
//                // Display the selected date and times in the main dialog
//                tvStart.text = startDate + " " + startTime
//                tvEnd.text = startDate + " " + endTime
//
//                dialog.dismiss()
//            }
//
//            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//
//            val alertDialog = alertDialogBuilder.create()
//            alertDialog.show()
//        }














































//        binding.AddAlertfloatingActionButton.setOnClickListener {
//
//            val alertDialogBuilder = AlertDialog.Builder(context)
//            alertDialogBuilder.setTitle("Alert setup")
//            alertDialogBuilder.setMessage("setup your alert")
//            alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
////                alertViewModel.removeFromALerts(product)
////                dialog.dismiss()
//            }
//            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//            val alertDialog = alertDialogBuilder.create()
//            alertDialog.show()
//
//
//
//
//
//
//
//            // Perform network call and insert data into the database
////            lifecycleScope.launch {
////                try {
////                    val weatherResponse = alertViewModel.makeNetworkCall()
////                    if (weatherResponse != null) {
////                        alertViewModel.addToAlerts(weatherResponse, weatherResponse.lon, weatherResponse.lat)
////                        Log.i("===db add", "onViewCreated: ")
////                    }
////                } catch (e: Exception) {
////                    // Handle network call or insertion error
////                    Log.e("===error in Alerts", "onViewCreated: ", e)
////                }
////            }
//        }








//class NotificationFragment : Fragment() {
//    private lateinit var floatingActionButton : FloatingActionButton
//    private lateinit var remoteDataSource: RemoteDataSource
//    private lateinit var localDataSourceInte: LocalDataSourceInte
//    private lateinit var alertViewModel: AlertViewModel
//    private lateinit var repository: WeatherRepositoryImpl
//    private lateinit var alertViewModelFactory: AlertViewModelFactory
////    private lateinit var homeViewModel: HomeFragmentViewModel
////    private lateinit var homeFactory : HomeFragmentViewModelFactory
//    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
//    private lateinit var alertsAdapter: AlertsAdapter
//    private lateinit var alertRecyclerView: RecyclerView
//    private lateinit var alertLayoutManager: LinearLayoutManager
//    private lateinit var binding : FragmentNotificationBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
//        remoteDataSource = RemoteDataSourceImp()
//        localDataSourceInte = LocalDataSourceImp(requireContext())
//        repository= WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)
//
//        alertViewModelFactory = AlertViewModelFactory(repository)
//        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
//        alertLayoutManager = LinearLayoutManager(requireContext())
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentNotificationBinding.inflate(inflater, container, false)
//        return binding.root
////        return inflater.inflate(R.layout.fragment_notification, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//
//        binding.alertsRecView.layoutManager = alertLayoutManager
//
//
//        alertsAdapter = AlertsAdapter(emptyList(),
//            { product, position ->
//                val alertDialogBuilder = AlertDialog.Builder(context)
//                alertDialogBuilder.setTitle("Delete Confirmation")
//                alertDialogBuilder.setMessage("Are you sure you want to remove this item?")
//                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
//                    alertViewModel.removeFromALerts(product)
//                    dialog.dismiss()
//                }
//                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.dismiss()
//                }
//                val alertDialog = alertDialogBuilder.create()
//                alertDialog.show()
//            },
//            { product, position ->
//
//                // Navigate to the new fragment
////
////                    val bundle = Bundle().apply {
////
////                        putDoubleArray("longlat", doubleArrayOf(product.longitude, product.latitude))
////                    }
////                    // Navigate to the new fragment
////                    val fragment = FavoriteDetailsFragment()
////                    fragment.arguments = bundle
////                    parentFragmentManager.beginTransaction()
////                        .replace(R.id.container, fragment)
////                        .addToBackStack(null)
////                        .commit()
//
////                replaceFragments(FavoriteDetailsFragment())
//                Toast.makeText(requireContext(),"on card", Toast.LENGTH_SHORT).show()
//            }
//        )
//
//        binding.alertsRecView.adapter = alertsAdapter
//        alertViewModel.showAlertsItems()
//
//        binding.AddAlertfloatingActionButton.setOnClickListener {
//            //network call
//
//            lifecycleScope.launch(Dispatchers.Main) {
//
//             alertViewModel.alertsData.collectLatest { weatherResponse ->
//              when (weatherResponse) {
//            is OneCallState.Suceess -> {
//
//                Log.i("==home fragment alert", ""+weatherResponse.data.alerts)
//                Log.i("==home fragment alert", ""+weatherResponse.data.current)
//
//                CoroutineScope(Dispatchers.IO).launch {
////                                db.getWeatherDAO().setAsFavorite(weatherResponse.data,weatherResponse.data.city.coord.lon,weatherResponse.data.city.coord.lat)
//                    alertViewModel.addToAlerts(
//                        weatherResponse.data,
//                        weatherResponse.data.lon,
//                        weatherResponse.data.lat
//                    )
//                    Log.i("===db add", "onViewCreated: ")
//                }
//
////                        repository.insertAlertIntoDB(weatherResponse,weatherResponse.data.lon,weatherResponse.data.lat)
//            }
//
//            is OneCallState.Loading -> {
//
//                Log.i("===lodaing in Alerts", "onViewCreated: ")
//
//            }
//
//            else -> {
//
//                Log.i("===error in Alerts", "onViewCreated: ")
//
//            }
//        }
//    }
//
//
//
//                lifecycleScope.launch(Dispatchers.Main) {
////            favoriteViewModel.showFavItems()
//
//                    alertViewModel.AlertData.collect { state ->
//                        when (state) {
//                            is ALertDBState.Loading -> {
////                       binding.animationView.visibility =View.VISIBLE
//                            }
//
//                            is  ALertDBState.Suceess -> {
//                                // Update the adapter with the new data
////                        binding.animationView.visibility =View.GONE
//
//                                alertsAdapter.submitList(state.alertdata)
//                                if(alertsAdapter.itemCount==0){
//                                    binding.animationView.visibility =View.VISIBLE
//                                }
//                                else {  binding.animationView.visibility =View.GONE}
//                            }
//
//                            else -> {}
//                        }
//                    }
//                }
//
//
//}
//
//
//
//
//
//            //get from database -> from adapter
//
//        }
//
//
//
//    }
//
//
//
//}












////todo move to alert fragment
//lifecycleScope.launch(Dispatchers.Main) {
//
//    viewModel.alertsData.collectLatest { weatherResponse ->
//        when (weatherResponse) {
//            is OneCallState.Suceess -> {
//
//                Log.i("==home fragment alert", ""+weatherResponse.data.alerts)
//                Log.i("==home fragment alert", ""+weatherResponse.data.current)
//
//                CoroutineScope(Dispatchers.IO).launch {
////                                db.getWeatherDAO().setAsFavorite(weatherResponse.data,weatherResponse.data.city.coord.lon,weatherResponse.data.city.coord.lat)
//                    viewModel.addToAlerts(
//                        weatherResponse.data,
//                        weatherResponse.data.lon,
//                        weatherResponse.data.lat
//                    )
//                    Log.i("===db add", "onViewCreated: ")
//                }
//
////                        repository.insertAlertIntoDB(weatherResponse,weatherResponse.data.lon,weatherResponse.data.lat)
//            }
//
//            is OneCallState.Loading -> {
//
//                Log.i("===lodaing in Alerts", "onViewCreated: ")
//
//            }
//
//            else -> {
//
//                Log.i("===error in Alerts", "onViewCreated: ")
//
//            }
//        }
//    }
//
//
//}