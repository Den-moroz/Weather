package com.example.weather

import android.app.Application
import com.example.weather.data.db.LocationRoomDatabase

class WeatherApplication : Application() {
    val database: LocationRoomDatabase by lazy { LocationRoomDatabase.getDatabase(this) }
}
