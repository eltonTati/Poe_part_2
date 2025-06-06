package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        drawerLayout = findViewById(R.id.main_drawer)
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)

        menuIcon.setOnClickListener() {
            drawerLayout.openDrawer(GravityCompat.END)

        }

          val languageText = findViewById<TextView>(R.id.languageText)
         languageText.setOnClickListener {
        val intent = Intent(this, LanguageSelectionActivity::class.java)
           startActivity(intent)
         }
        val contactSupport = findViewById<TextView>(R.id.contactSupport)
        contactSupport .setOnClickListener {
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
        }
        val cat = findViewById<TextView>(R.id.cat)
         cat.setOnClickListener {
          val intent = Intent(this, CategoryActivity::class.java)
          startActivity(intent)
        }
         val acc= findViewById<TextView>(R.id.acc)
         acc.setOnClickListener { val intent = Intent(this, AccountActivity::class.java)
          startActivity(intent)
         }
         val curr = findViewById<TextView>(R.id.curr)
        curr.setOnClickListener {
            val intent = Intent(this, CurrencySettingsActivity::class.java)
          startActivity(intent)
          }
         val rank = findViewById<TextView>(R.id.rank)
         rank.setOnClickListener {
         val intent = Intent(this, MyRankingActivity::class.java)
          startActivity(intent)
         }
        val wallet = findViewById<ImageView>(R.id.navTransactions)
        wallet.setOnClickListener {
            val intent = Intent(this, WalletScreenActivity::class.java)
            startActivity(intent)
        }
        val analytics = findViewById<ImageView>(R.id.navAnalysis)
        analytics.setOnClickListener {
            val intent = Intent(this, AnalyticsScreenActivity::class.java)
            startActivity(intent)
        }
        val user = findViewById<ImageView>(R.id.nav_settings)
        user.setOnClickListener {
            val intent = Intent(this, UserScreenActivity::class.java)
            startActivity(intent)
        }

    }
}
