package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.model.WeatherHour
import com.example.weather.service.WeatherIcon
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HourlyAdapter(private val items: List<WeatherHour>) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hourTxt: TextView = itemView.findViewById(R.id.hour_time)
        val tempTxt: TextView = itemView.findViewById(R.id.hour_temp)
        val imageView: ImageView = itemView.findViewById(R.id.hour_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("h a")
        val time = LocalTime.parse(item.datetime, inputFormatter)

        val formattedTime = time.format(outputFormatter)

        holder.hourTxt.text = formattedTime
        holder.tempTxt.text = item.temp.toString()

        val context = holder.itemView.context
        val weatherIcon = WeatherIcon.getIconByCode(item.icon)

        Glide.with(context)
            .load(weatherIcon.iconResourceId)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
