package com.example.weatherapppoject.alerts

//class AlertViewModel(private val weathRepo: WeatherRepositoryImpl): ViewModel() {
//    //alert
//    private val _alerts = MutableStateFlow<ALertDBState>(ALertDBState.Loading())
//    val AlertData: StateFlow<ALertDBState> = _alerts
//
//    private val _alertData = MutableStateFlow<OneCallState>(OneCallState.Loading())
//    val alertsData: StateFlow<OneCallState> = _alertData
//
////        //==========>network call
////    fun getAlertsInfo(latitude: Double, longitude: Double) {
////        viewModelScope.launch(Dispatchers.IO) {
////            val units = "metric"
////            val apiKey = Utils.APIKEY
////            val lang = "en"
////            weathRepo.getAlertData(46.8182, 8.2275, units, apiKey, lang).collect {
//////            weatherRepository.getAlertData(31.2683793, 30.006182, units, apiKey, lang).collect {
////                _alertData.value = OneCallState.Suceess(it)
////            }
////        }
////    }
////
////    //============?data base
////    fun addToAlerts(alerts: OneApiCall, long: Double, lat: Double) {
////        viewModelScope.launch(Dispatchers.IO) {
////            weathRepo.insertAlertIntoDB(alerts, long, lat)
////            Log.i("=======", "addToFavorites: done")
//////            withContext(Dispatchers.Main){
//////                Toast.makeText(context,"item added",Toast.LENGTH_SHORT).show()
//////            }
////        }
////    }
////
////
////
////
////    fun removeFromALerts(weatherData: OneApiCall) {
////        viewModelScope.launch(Dispatchers.IO) {
////            weathRepo.deleteFromAlerts(weatherData)
////        }
////    }
////
////
////    fun showAlertsItems() {
////        viewModelScope.launch(Dispatchers.IO) {
////
////            weathRepo.getAlertedData()
////                .catch { exception ->
////                    _alerts.value = ALertDBState.Failure(exception)
////                }
////                ///edits here==> products.collect
////                .collect { data ->
////                    _alerts.value = ALertDBState.Suceess(data)
////                }
////        }
////    }
////
////
////
////
////
////
////
////
////
//
//
//
//}