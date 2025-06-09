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

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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
                                    // 🔁 Vérifier ou créer balances
                                    val balancesRef = db.collection("balances").document(uid)
                                    balancesRef.get().addOnSuccessListener { doc ->
                                        if (!doc.exists()) {
                                            val balance = hashMapOf(
                                                "userId" to uid,
                                                "total" to 0.0,
                                                "expenses" to 0.0
                                            )
                                            balancesRef.set(balance)
                                        }
                                    }

                                    // 🔁 Vérifier ou créer budget_goals
                                    val goalsRef = db.collection("budget_goals").document(uid)
                                    goalsRef.get().addOnSuccessListener { doc ->
                                        if (!doc.exists()) {
                                            val budgetGoal = hashMapOf(
                                                "userId" to uid,
                                                "amount" to 0.0,
                                                "budgetName" to "Default Goal",
                                                "recurrence" to "Monthly"
                                            )
                                            goalsRef.set(budgetGoal)
                                        }
                                    }

                                    // 🔁 Ajouter catégories (si non déjà créées)
                                    db.collection("categories")
                                        .whereEqualTo("userId", uid)
                                        .get()
                                        .addOnSuccessListener { existing ->
                                            if (existing.isEmpty) {
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
                                            }
                                        }

                                    // 🔁 Vérifier ou créer account
                                    val accountRef = db.collection("accounts").document(uid)
                                    accountRef.get().addOnSuccessListener { doc ->
                                        if (!doc.exists()) {
                                            val account = hashMapOf(
                                                "userId" to uid,
                                                "accountName" to "N/A",
                                                "accountNumber" to "N/A",
                                                "accountType" to "N/A",
                                                "logo" to "N/A"
                                            )
                                            accountRef.set(account)
                                        }
                                    }

                                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
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
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)// add elton
            startActivity(intent)
        }
    }
}