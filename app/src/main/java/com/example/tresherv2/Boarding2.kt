package com.example.tresherv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Boarding2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boarding2)
        val nexttext = findViewById<TextView>(R.id.tv_next)
        nexttext.setOnClickListener {
            val intent = Intent(this, Boarding3::class.java)
            startActivity(intent)
        }
    }
}