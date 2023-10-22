package com.example.weather.service

import android.app.Application
import com.example.weather.data.LocationRoomDatabase

class WeatherApplication : Application() {
    val database: LocationRoomDatabase by lazy { LocationRoomDatabase.getDatabase(this) }
}
