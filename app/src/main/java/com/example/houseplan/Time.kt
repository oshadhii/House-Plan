package com.example.houseplan

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Time : AppCompatActivity() {

    private var startTime: Long? = null
    private var timeInMilliseconds: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var updateTime: Long = 0L
    private val handler = Handler()
    private var isRunning = false

    private lateinit var timerDisplay: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button

    private val updateTimerThread = object : Runnable {
        override fun run() {
            startTime?.let { start ->
                timeInMilliseconds = SystemClock.uptimeMillis() - start
                updateTime = timeSwapBuff + timeInMilliseconds

                val secs = (updateTime / 1000).toInt()
                val mins = secs / 60
                val milliseconds = (updateTime % 1000).toInt()

                timerDisplay.text = String.format("%02d:%02d:%03d", mins, secs % 60, milliseconds)
                handler.postDelayed(this, 0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        timerDisplay = findViewById(R.id.timer_display)
        startButton = findViewById(R.id.button_start)
        pauseButton = findViewById(R.id.pause)
        resetButton = findViewById(R.id.restart)

        startButton.setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(updateTimerThread, 0)
                pauseButton.visibility = View.VISIBLE
                startButton.visibility = View.GONE
                isRunning = true
            }
        }

        pauseButton.setOnClickListener {
            if (isRunning) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(updateTimerThread)
                pauseButton.visibility = View.GONE
                startButton.text = "Resume"
                startButton.visibility = View.VISIBLE
                isRunning = false
            }
        }

        resetButton.setOnClickListener {
            startTime = null
            timeSwapBuff = 0L
            timeInMilliseconds = 0L
            updateTime = 0L
            handler.removeCallbacks(updateTimerThread)
            timerDisplay.text = "00:00:00"
            startButton.text = "Start"
            startButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
            isRunning = false
        }
    }
}