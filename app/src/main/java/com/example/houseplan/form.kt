package com.example.houseplan

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class form : AppCompatActivity() {
    private lateinit var timeInput: TextView
    private lateinit var setButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        timeInput = findViewById(R.id.settime)
        setButton = findViewById(R.id.setAlarm)

        setButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                // Update the TextView with the selected time
                timeInput.text = String.format("%02d:%02d", selectedHour, selectedMinute)

                // Set the alarm for the selected time
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                calendar.set(Calendar.SECOND, 0)

                setAlarm(calendar.timeInMillis)

            }, hour, minute, true)

            timePickerDialog.show()
        }

        val Workout = findViewById<EditText>(R.id.editTextText)
        val Reps = findViewById<EditText>(R.id.editTextText2)
        val Date = findViewById<EditText>(R.id.editTextText3)
        val Time = findViewById<EditText>(R.id.editTextText4)
        val addButton = findViewById<Button>(R.id.button3)

        addButton.setOnClickListener {
            val workout = Workout.text.toString()
            val reps = Reps.text.toString()
            val date = Date.text.toString()
            val time = Time.text.toString()

            val sharedPreferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("sessionList", null)
            val type = object : TypeToken<MutableList<homeData>>() {}.type
            val sessionList: MutableList<homeData> = if (json != null) gson.fromJson(json, type) else mutableListOf()

            sessionList.add(homeData(workout, reps, date, time))

            val updatedJson = gson.toJson(sessionList)
            val editor = sharedPreferences.edit()
            editor.putString("sessionList", updatedJson)
            editor.apply()

            finish()
        }
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)  // Intent to trigger alarm receiver

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Check Android version for exact alarm scheduling
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {  // Android 12+
            // Handle exact alarm permission for Android 12+
            if (alarmManager.canScheduleExactAlarms()) {
                // Schedule the exact alarm
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
                Toast.makeText(this, "Alarm set for: " + timeInput.text, Toast.LENGTH_SHORT).show()
            } else {
                // Redirect user to settings to enable exact alarm permission
                Toast.makeText(this, "Please enable exact alarms in the settings", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        } else {
            // Schedule the alarm for devices below Android 12 (no permission required)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
            Toast.makeText(this, "Alarm set for: " + timeInput.text, Toast.LENGTH_SHORT).show()
        }
    }

}