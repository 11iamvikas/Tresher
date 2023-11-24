package com.example.tresherv2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class CollectActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)
        val reportbutton = findViewById<Button>(R.id.btn_report)
        reportbutton.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
        val homebutton = findViewById<Button>(R.id.btn_home)
        homebutton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        val communitybutton = findViewById<Button>(R.id.btn_community)
        communitybutton.setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)
        }
        val collectbutton = findViewById<Button>(R.id.btn_collect)
        collectbutton.setOnClickListener {
            val intent = Intent(this, CollectActivity::class.java)
            startActivity(intent)
        }
    }
}