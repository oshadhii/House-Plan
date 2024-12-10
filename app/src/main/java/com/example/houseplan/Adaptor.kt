package com.example.houseplan

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.view.LayoutInflater

import android.view.ViewGroup

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Adaptor(
    private val context: Context,
    private var homeList: MutableList<homeData>
) : RecyclerView.Adapter<Adaptor.homeViewHolder>() {

    // ViewHolder definition
    class homeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleView)
        val frequency: TextView = itemView.findViewById(R.id.freqView)
        val timeView: TextView = itemView.findViewById(R.id.dateView)
        val updateButton: Button = itemView.findViewById(R.id.updateBtn)
        val deleteButton: Button = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):homeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.read_view, parent, false)
        return homeViewHolder(view)
    }

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {
        val home = homeList[position]
        holder.title.text = home.title
        holder.frequency.text = home.frequency
        holder.timeView.text = home.date

        holder.deleteButton.setOnClickListener {
            removeMedicine(position)
        }

        holder.updateButton.setOnClickListener {
            updateMedicineData(position)
        }
    }

    override fun getItemCount(): Int {
        return homeList.size
    }

    fun reloadData() {
        val sharedPreferences = context.getSharedPreferences("MedicinePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("medicineList", null)
        val type = object : TypeToken<MutableList<homeData>>() {}.type
        homeList = if (json != null) gson.fromJson(json, type) else mutableListOf()
        notifyDataSetChanged()
    }

    private fun removeMedicine(position: Int) {
        homeList.removeAt(position)
        notifyItemRemoved(position)

        val sharedPreferences = context.getSharedPreferences("MedicinePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val updatedJson = gson.toJson(homeList)
        val editor = sharedPreferences.edit()
        editor.putString("medicineList", updatedJson)
        editor.apply()

        notifyItemRangeChanged(position, homeList.size)
    }

    private fun updateMedicineData(position: Int) {
        val home = homeList[position]
        val intent = Intent(context, FormUpdate::class.java)
        intent.putExtra("position", position)
        intent.putExtra("date", home.title)
        intent.putExtra("medicineName", home.frequency)
        intent.putExtra("doseQuantity", home.date)
        context.startActivity(intent)
        }
}

//
//val titleView: TextView = itemView.findViewById(R.id.titleView)
//val freqView: TextView = itemView.findViewById(R.id.freqView)
//val timeView: TextView = itemView.findViewById(R.id.dateView)
//val updateButton: Button = itemView.findViewById(R.id.updateBtn)
//val deleteButton: Button = itemView.findViewById(R.id.deleteBtn)

