package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        drawerLayout = findViewById(R.id.main_drawer)
        val menuIcon = findViewById<ImageView>(R.id.menu_icon)

        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(R.id.custom_nav_drawer2)
        }
        val acc= findViewById<TextView>(R.id.acc)
        acc.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
        val languageText = findViewById<TextView>(R.id.languageText)
        languageText.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
        val contactSupport = findViewById<TextView>(R.id.contactSupport)
        contactSupport .setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
        val cat = findViewById<TextView>(R.id.cat)
        cat.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
        val curr = findViewById<TextView>(R.id.curr)
        curr.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
        val rank = findViewById<TextView>(R.id.rank)
        rank.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
    }
}
