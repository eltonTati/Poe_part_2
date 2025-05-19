package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish() // Go back to previous screen
        }

        // Plus Button (Add Category)
        val addButton = findViewById<ImageButton>(R.id.addCategoryButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddCategoryScreenActivity::class.java)
            startActivity(intent)
        }
    }
}
