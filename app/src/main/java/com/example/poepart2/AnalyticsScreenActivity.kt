package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Transaction


class AnalyticsScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_screen)
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<ImageView>(R.id.navAnalysis).setOnClickListener {
            startActivity(Intent(this, AnalyticsScreenActivity::class.java))
        }
        findViewById<ImageView>(R.id.navTransactions).setOnClickListener {
            startActivity(Intent(this, WalletScreenActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, UserScreenActivity::class.java))
        }
        }

    }
