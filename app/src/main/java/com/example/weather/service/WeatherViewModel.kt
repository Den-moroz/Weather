package com.example.weather.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.data.DataStoreManager
import com.example.weather.data.Location
import com.example.weather.data.LocationDao
import com.example.weather.model.WeatherForLocationResponse
import com.example.weather.model.WeatherResponse
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

const val TAG = "WeatherViewModel";

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel(private val locationDao: LocationDao, private val dataStoreManager: DataStoreManager): ViewModel() {
    private val _dailyWeatherData = MutableLiveData<WeatherResponse>()
    val dailyWeatherData: LiveData<WeatherResponse> get() = _dailyWeatherData

    private val _weeklyWeatherData = MutableLiveData<WeatherResponse>()
    val weeklyWeatherData: LiveData<WeatherResponse> get() = _weeklyWeatherData

    private val _status = MutableLiveData<WeatherApiStatus>()

    val status: LiveData<WeatherApiStatus> = _status

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    val locations: LiveData<List<Location>> = locationDao.getAll().asLiveData()

    private val _weatherForLocation = MutableLiveData<List<WeatherForLocationResponse>>()
    val weatherForLocation: LiveData<List<WeatherForLocationResponse>> get() = _weatherForLocation

    private val _selectedLocationIndex = MutableLiveData<Int?>()
    var selectedLocationIndex: Int?
        get() = _selectedLocationIndex.value
        set(value) {
            _selectedLocationIndex.value = value
        }

    private val weatherApi = WeatherApi.create()

    init {
        viewModelScope.launch {
            dataStoreManager.locationFlow.collect { location ->
                _location.value = location
            }
        }
    }

    fun updateLocation(newLocation: String) {
        viewModelScope.launch {
            dataStoreManager.saveLocation(newLocation)
        }
    }

    fun getDailyWeather() {
        val locationValue = location.value
        if (locationValue != null) {
            viewModelScope.launch {
                _status.value = WeatherApiStatus.LOADING
                try {
                    _dailyWeatherData.value = weatherApi.getDailyWeather(locationValue)
                    _status.value = WeatherApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = WeatherApiStatus.ERROR
                    Log.e(
                        TAG,
                        "Something went wrong when trying to execute network request for daily weather"
                    )
                }
            }
        }
    }

    fun getWeeklyWeatherData() {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                val locationValue = location.value ?: "London"
                _weeklyWeatherData.value = weatherApi.getWeeklyWeather(locationValue)
                _status.value = WeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
                Log.e(TAG, "Something went wrong when trying to execute network request for weekly weather")
            }
        }
    }

    fun getWeatherForLocations() {
        if (locations.value != null) {
            viewModelScope.launch {
                _status.value = WeatherApiStatus.LOADING
                try {
                    val listOfWeather: MutableList<WeatherForLocationResponse> = mutableListOf()
                    for (location in locations.value!!) {
                        listOfWeather.add(weatherApi.getWeatherForCity(location.locationName))
                    }
                    _weatherForLocation.value = listOfWeather
                    _status.value = WeatherApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = WeatherApiStatus.ERROR
                    Log.e(
                        TAG,
                        "Something went wrong when trying to execute network request for daily weather"
                    )
                }
            }
        }
    }

    fun insertLocation(location: Location) {
        viewModelScope.launch {
            locationDao.insert(location)
        }
    }

    fun deleteLocationAt(index: Int) {
        val locationToDelete = locations.value?.get(index)
        if (locationToDelete != null) {
            viewModelScope.launch {
                locationDao.delete(locationToDelete)
            }
        }
    }
}

class WeatherViewModelFactory(private val locationDao: LocationDao, private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(locationDao, dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}