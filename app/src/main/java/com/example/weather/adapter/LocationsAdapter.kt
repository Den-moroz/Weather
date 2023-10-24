package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.model.WeatherForLocationResponse
import com.example.weather.service.LocationsViewModel
import com.example.weather.service.WeatherViewModel

class LocationsAdapter(
    private val items: List<WeatherForLocationResponse>,
    private val viewModel: LocationsViewModel,
    private val weatherViewModel: WeatherViewModel) : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val savedLocation: TextView = itemView.findViewById(R.id.saved_location)
        val currentWeatherConditions: TextView = itemView.findViewById(R.id.current_weather_conditions)
        val currentWeatherTemp: TextView = itemView.findViewById(R.id.current_weather_temp)
        val currentWeatherTempMaxAndMin: TextView = itemView.findViewById(R.id.current_weather_temp_max_and_min)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        val refactoredTimezone = item.timezone.replace("/", " | ").replace("_", " ")
        holder.savedLocation.text = refactoredTimezone
        holder.currentWeatherConditions.text = item.days[0].conditions
        holder.currentWeatherTemp.text = holder.itemView.context.getString(R.string.label_temperature,
            item.days[0].temp.toString()
        )
        holder.currentWeatherTempMaxAndMin.text = holder.itemView.context.getString(
            R.string.label_temp_max_and_min,
            item.days[0].tempmax.toString(),
            item.days[0].tempmin.toString()
        )

        holder.itemView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                viewModel.selectedLocationIndex = position
            }
            false
        }

        holder.itemView.setOnClickListener {
            weatherViewModel.updateLocation(item.timezone.split("/")[1])
            holder.itemView.findNavController().navigate(R.id.action_locationsFragment_to_dailyWeatherFragment)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
