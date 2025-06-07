package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MyRankingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_rankings)
        val rank = findViewById<TextView>(R.id.SeeMilestone)
        rank.setOnClickListener {
            val intent = Intent(this, RankMilestoneActivity::class.java)
            startActivity(intent)
        }
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            finish()
        }
        }

    }
