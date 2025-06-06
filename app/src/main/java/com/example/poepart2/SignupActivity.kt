package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var nameEditText: TextInputEditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialiser Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Associer les vues
        emailEditText = findViewById(R.id.signup_email)
        passwordEditText = findViewById(R.id.signup_password)
        nameEditText = findViewById(R.id.signup_name)
        signupButton = findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid

                        val user = hashMapOf(
                            "name" to name,
                            "email" to email
                        )

                        userId?.let { uid ->
                            db.collection("users").document(uid).set(user)
                                .addOnSuccessListener {
                                    // ✅ Créer balances
                                    val balance = hashMapOf(
                                        "userId" to uid,
                                        "totalBalance" to "N/A",
                                        "actualBalance" to "N/A"
                                    )
                                    db.collection("balances").document(uid).set(balance)

                                    // ✅ Créer budget_goals
                                    val budgetGoal = hashMapOf(
                                        "userId" to uid,
                                        "amount" to "N/A",
                                        "budgetName" to "N/A",
                                        "recurrence" to "N/A"
                                    )
                                    db.collection("budget_goals").document(uid).set(budgetGoal)

                                    // ✅ Créer categories prédéfinies
                                    val categories = listOf(
                                        hashMapOf("userId" to uid, "categoryName" to "Salary", "categoryType" to "Income"),
                                        hashMapOf("userId" to uid, "categoryName" to "Investment", "categoryType" to "Income"),
                                        hashMapOf("userId" to uid, "categoryName" to "Rent", "categoryType" to "Expenses"),
                                        hashMapOf("userId" to uid, "categoryName" to "Groceries", "categoryType" to "Expenses"),
                                        hashMapOf("userId" to uid, "categoryName" to "Electricities", "categoryType" to "Expenses")
                                    )
                                    for (cat in categories) {
                                        db.collection("categories").add(cat)
                                    }

                                    // ✅ Créer accounts vide
                                    val account = hashMapOf(
                                        "userId" to uid,
                                        "accountName" to "N/A",
                                        "accountNumber" to "N/A",
                                        "accountType" to "N/A",
                                        "logo" to "N/A"
                                    )
                                    db.collection("accounts").document(uid).set(account)

                                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MenuActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error saving user: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}