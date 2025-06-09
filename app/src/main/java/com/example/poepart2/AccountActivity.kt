package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load your layout
        setContentView(R.layout.activity_account_management)

        // 🔙 Back button click
        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }

        // ⬇️ Dropdown icon click
        findViewById<ImageView>(R.id.dropdown_icon).setOnClickListener {
            Toast.makeText(this, "comming soon", Toast.LENGTH_SHORT).show()
        }

        // ➕ Add Account button click
        findViewById<Button>(R.id.add_account_btn).setOnClickListener {
            startActivity(Intent(this, AddNewAccountActivity::class.java))

            //
        }
    }
}
