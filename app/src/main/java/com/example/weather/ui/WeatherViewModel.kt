package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.WeatherResponse
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

const val TAG = "WeatherViewModel";

class WeatherViewModel: ViewModel() {
    private val _dailyWeatherData = MutableLiveData<WeatherResponse>()
    val dailyWeatherData: LiveData<WeatherResponse> get() = _dailyWeatherData

    private val _weeklyWeatherData = MutableLiveData<WeatherResponse>()
    val weeklyWeatherData: LiveData<WeatherResponse> get() = _weeklyWeatherData

    private val weatherApi = WeatherApi.create()


    fun getDailyWeather() {
        viewModelScope.launch {
            try {
                _dailyWeatherData.value = weatherApi.getDailyWeather()
            } catch (e: Exception) {
                Log.e(TAG, "Something went wrong when trying to execute network request for daily weather")
            }
        }
    }

    fun getWeeklyWeatherData() {
        viewModelScope.launch {
            try {
                _weeklyWeatherData.value = weatherApi.getWeeklyWeather()
            } catch (e: Exception) {
                Log.e(TAG, "Something went wrong when trying to execute network request for weekly weather")
            }
        }
    }
}