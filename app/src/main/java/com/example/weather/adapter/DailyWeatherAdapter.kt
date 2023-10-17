package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.model.WeatherDay
import com.example.weather.model.WeatherHour

class DailyWeatherAdapter(private val weatherData: List<WeatherDay>) : RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherNameTextView: TextView = itemView.findViewById(R.id.current_weather)
        val weatherImageView: ImageView = itemView.findViewById(R.id.current_weather_image)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val temperatureTextView: TextView = itemView.findViewById(R.id.temperature)
        val maxAndMinTextView: TextView = itemView.findViewById(R.id.current_max_and_min_temperature)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyWeatherAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyWeatherAdapter.ViewHolder, position: Int) {
        val hour = weatherData[position]
        holder.temperatureTextView.text = "${hour.temp} °C"
        holder.maxAndMinTextView.text = "Humidity: ${hour.humidity}%"
        holder.dateTextView.text = "${hour.datetime}"

    }

    override fun getItemCount(): Int {
        return weatherData.size
    }
}