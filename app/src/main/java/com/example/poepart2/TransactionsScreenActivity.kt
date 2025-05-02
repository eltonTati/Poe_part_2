package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class TransactionsScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_screen)
        val backMenuButton2 = findViewById<ImageButton>(R.id.back_menu_button2)
        backMenuButton2.setOnClickListener {
            finish()
        }
        val sideMenuButton2 = findViewById<ImageButton>(R.id.side_menu_button2)
        sideMenuButton2.setOnClickListener {
            val intent = Intent(this, TransactionsScreenActivity::class.java)
        }
    }
}