package com.example.houseplan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var btn1= findViewById<ImageView>(R.id.add)
        btn1.setOnClickListener{
            val intent1= Intent(this,form::class.java)
            startActivity(intent1)
        }
        var btn2= findViewById<ImageView>(R.id.stop)
        btn1.setOnClickListener{
            val intent1= Intent(this,Time::class.java)
            startActivity(intent1)
        }
    }
}