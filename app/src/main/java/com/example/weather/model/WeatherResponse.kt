package com.example.weather.model

data class WeatherResponse(
    val days: List<WeatherDay>
)

data class WeatherDay(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val humidity: Double,
    val windspeed: Double,
    val hours: List<WeatherHour>
)

data class WeatherHour(
    val datetime: String,
    val temp: Double,
    val humidity: Double,
    val windspeed: Double
)