package com.example.poepart2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddCategoryScreenActivity : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var categoryTypeEditText: EditText
    private lateinit var categoryTypeIcon: ImageView
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val categoryTypes = arrayOf("Income", "Expenses")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category_screen)

        categoryNameEditText = findViewById(R.id.editTextTextPersonName2)
        categoryTypeEditText = findViewById(R.id.categoryTypeEditText)
        categoryTypeIcon = findViewById(R.id.categoryTypeIcon)
        addButton = findViewById(R.id.button)
        cancelButton = findViewById(R.id.button2)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Drop-down logic
        val dropDown = View.OnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose Category Type")
            builder.setItems(categoryTypes) { _, which ->
                categoryTypeEditText.setText(categoryTypes[which])
            }
            builder.show()
        }

        categoryTypeEditText.setOnClickListener(dropDown)
        categoryTypeIcon.setOnClickListener(dropDown)

        addButton.setOnClickListener {
            val name = categoryNameEditText.text.toString().trim()
            val type = categoryTypeEditText.text.toString().trim()

            if (name.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid ?: return@setOnClickListener
            val category = hashMapOf(
                "userId" to userId,
                "categoryName" to name,
                "categoryType" to type
            )

            db.collection("categories")
                .add(category)
                .addOnSuccessListener {
                    Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
                    categoryNameEditText.text.clear()
                    categoryTypeEditText.text.clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        cancelButton.setOnClickListener {
            finish() // close the activity
        }
    }
}

