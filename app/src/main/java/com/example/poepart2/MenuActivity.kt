package com.example.poepart2

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var settingsButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        drawerLayout = findViewById(R.id.main_drawer)
        val menuIcon = findViewById<ImageView>(R.id.menu_icon)

        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(R.id.custom_nav_drawer2)
        }
    }
}
