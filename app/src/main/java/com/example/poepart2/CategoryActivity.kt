package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CategoryActivity : AppCompatActivity() {

    private lateinit var incomeContainer: LinearLayout
    private lateinit var expensesContainer: LinearLayout
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category) // Replace with your actual XML file name if needed

        incomeContainer = findViewById(R.id.incomeItemsContainer)
        expensesContainer = findViewById(R.id.expensesItemsContainer)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser ?: return

        db.collection("categories")
            .whereEqualTo("userId", currentUser.uid)
            .addSnapshotListener { snapshot, error ->
                incomeContainer.removeAllViews()
                expensesContainer.removeAllViews()

                if (snapshot == null || error != null || snapshot.isEmpty) return@addSnapshotListener

                for (doc in snapshot.documents) {
                    val name = doc.getString("categoryName") ?: continue
                    val type = doc.getString("categoryType") ?: continue

                    val row = LinearLayout(this).apply {
                        orientation = LinearLayout.HORIZONTAL
                        setPadding(12, 12, 12, 12)
                        setBackgroundColor(ContextCompat.getColor(this@CategoryActivity, R.color.wisesaverbackground))
                    }

                    val nameText = TextView(this).apply {
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        text = name
                        textSize = 16f
                        setTextColor(resources.getColor(android.R.color.white))
                    }

                    val icon = ImageView(this).apply {
                        layoutParams = LinearLayout.LayoutParams(48, 48)
                        setImageResource(R.drawable.menuselct)
                    }

                    row.addView(nameText)
                    row.addView(icon)

                    if (type == "Income") {
                        incomeContainer.addView(row)
                    } else if (type == "Expenses") {
                        expensesContainer.addView(row)
                    }
                }
            }

        val addButton = findViewById<ImageButton>(R.id.addCategoryButton)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddCategoryScreenActivity::class.java))
        }
    }
}

