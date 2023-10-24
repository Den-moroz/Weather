package com.example.weather.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Location
import com.example.weather.data.LocationDao
import com.example.weather.model.WeatherForLocationResponse
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

class LocationsViewModel(private val locationDao: LocationDao): ViewModel() {
    val locations: LiveData<List<Location>> = locationDao.getAll().asLiveData()

    private val _weatherForLocation = MutableLiveData<List<WeatherForLocationResponse>>()
    val weatherForLocation: LiveData<List<WeatherForLocationResponse>> get() = _weatherForLocation

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus> = _status

    private val _selectedLocationIndex = MutableLiveData<Int?>()
    var selectedLocationIndex: Int?
        get() = _selectedLocationIndex.value
        set(value) {
            _selectedLocationIndex.value = value
        }

    private val weatherApi = WeatherApi.create()

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

class LocationsViewModelFactory(private val locationDao: LocationDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationsViewModel(locationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
