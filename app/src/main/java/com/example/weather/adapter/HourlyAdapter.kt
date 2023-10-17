package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.model.Hourly

class HourlyAdapter(private val items: ArrayList<Hourly>) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hourTxt: TextView = itemView.findViewById(R.id.hour_time)
        val tempTxt: TextView = itemView.findViewById(R.id.hour_image)
        val imageView: ImageView = itemView.findViewById(R.id.hour_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.hourTxt.text = item.hour.toString()
        holder.tempTxt.text = item.temp.toString()

        val context = holder.itemView.context
        val drawableResourceId = context.resources.getIdentifier(item.picPath, "drawable", context.packageName)
        Glide.with(context)
            .load(drawableResourceId)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}