package com.example.poepart2

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
        findViewById<ImageView>(R.id.back_icon).setOnClickListener {
            Toast.makeText(this, "Back pressed", Toast.LENGTH_SHORT).show()
        }

        // ⬇️ Dropdown icon click
        findViewById<ImageView>(R.id.dropdown_icon).setOnClickListener {
            Toast.makeText(this, "Dropdown clicked", Toast.LENGTH_SHORT).show()
        }

        // ➕ Add Account button click
        findViewById<Button>(R.id.add_account_btn).setOnClickListener {
            Toast.makeText(this, "Add Account clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
