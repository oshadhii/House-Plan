package com.example.houseplan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FormUpdate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_update)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btn4= findViewById<Button>(R.id.update)
        btn4.setOnClickListener{
            val intent4= Intent(this,list::class.java)
            startActivity(intent4)
        }

        val position = intent.getIntExtra("position", -1)
        val oldTitle = intent.getStringExtra("title")
        val oldDescription = intent.getStringExtra("description")
        val oldFrequency = intent.getStringExtra("frequency")
        val oldDate = intent.getStringExtra("date")


        val titleInput = findViewById<EditText>(R.id.title)
        val descriptionInput = findViewById<EditText>(R.id.description)
        val frequencyInput = findViewById<EditText>(R.id.frequency)
        val dateInput = findViewById<EditText>(R.id.date)
        val updateBtn = findViewById<Button>(R.id.update)


        titleInput.setText(oldTitle)
        descriptionInput.setText(oldDescription)
        frequencyInput.setText(oldFrequency)
        dateInput.setText(oldDate)

        updateBtn.setOnClickListener {
            val newtitle = titleInput.text.toString()
            val newdescription = descriptionInput.text.toString()
            val newfrequency = frequencyInput.text.toString()
            val newDate = dateInput.text.toString()

            val sharedPreferences = getSharedPreferences("MedicinePrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("medicineList", null)
            val type = object : TypeToken<MutableList<homeData>>() {}.type
            val medicineList: MutableList<homeData> =
                if (json != null) gson.fromJson(json, type) else mutableListOf()

            if (position != -1 && position < medicineList.size) {
                medicineList[position] = homeData(newtitle, newdescription, newfrequency, newDate)
            }

            val updatedJson = gson.toJson(medicineList)
            val editor = sharedPreferences.edit()
            editor.putString("medicineList", updatedJson)
            editor.apply()

            finish()
        }
    }
}