package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.WeatherDay
import com.example.weather.model.WeatherResponse
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

const val TAG = "WeatherViewModel";

class WeatherViewModel: ViewModel() {
    private val weatherApi = WeatherApi.create()

    private val _weather = MutableLiveData<WeatherDay>()
    val weather: LiveData<WeatherDay>
        get() = _weather

    private val _currentWeather = MutableLiveData<String>()
    val currentWeather: LiveData<String> = _currentWeather

    private val _weatherIcon = MutableLiveData<Int>()
    val weatherIcon: LiveData<Int> = _weatherIcon

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private val _currentTemperature = MutableLiveData<String>()
    val currentTemperature: LiveData<String> = _currentTemperature

    private val _currentMaxMinTemperature = MutableLiveData<String>()
    val currentMaxMinTemperature: LiveData<String> = _currentMaxMinTemperature

    init {
        getDailyWeather()
    }

    fun getDailyWeather() {
        viewModelScope.launch {
            try {
                _weather.value = weatherApi.getDailyWeather().days[0]
            } catch (e: Exception) {
                Log.d(TAG, "Something went wrong when trying to execute network request")
            }
        }
    }
}