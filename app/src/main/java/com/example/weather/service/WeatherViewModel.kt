package com.example.weather.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.data.DataStoreManager
import com.example.weather.model.WeatherResponse
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

const val TAG = "ViewModel";

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel(private val dataStoreManager: DataStoreManager): ViewModel() {
    private val _dailyWeatherData = MutableLiveData<WeatherResponse>()
    val dailyWeatherData: LiveData<WeatherResponse> get() = _dailyWeatherData

    private val _weeklyWeatherData = MutableLiveData<WeatherResponse>()
    val weeklyWeatherData: LiveData<WeatherResponse> get() = _weeklyWeatherData

    private val _status = MutableLiveData<WeatherApiStatus>()

    val status: LiveData<WeatherApiStatus> = _status

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

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
            _location.value = newLocation
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
                Log.e(
                    TAG,
                    "Something went wrong when trying to execute network request for weekly weather"
                )
            }
        }
    }
}

class WeatherViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
