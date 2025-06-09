package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

private var userListener: ListenerRegistration? = null
private lateinit var db: FirebaseFirestore
private lateinit var auth: FirebaseAuth
private lateinit var welcomeText: TextView

class UserScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        welcomeText = findViewById(R.id.welcome_text)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // hi text
            userListener = db.collection("users").document(userId)
                .addSnapshotListener { document, _ ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "User"
                        welcomeText.text = "Hi, $name"
                    }
                }
        findViewById<Button>(R.id.btn_profile_settings).setOnClickListener {
            Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btn_profile_settings2).setOnClickListener {
            Toast.makeText(this, "Notifications comming soon", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_help).setOnClickListener {
            startActivity(Intent(this, SupportActivity::class.java))
        }

        findViewById<Button>(R.id.btn_contact).setOnClickListener {
            startActivity(Intent(this, SupportActivity::class.java))
        }

        findViewById<Button>(R.id.btn_sign_out).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
        }

        // Bottom navigation
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageView>(R.id.navAnalysis).setOnClickListener {
            startActivity(Intent(this, AnalyticsScreenActivity::class.java))
            Toast.makeText(this, "Stats", Toast.LENGTH_SHORT).show()

        }

        findViewById<ImageView>(R.id.navTransactions).setOnClickListener {
            startActivity(Intent(this, WalletScreenActivity::class.java))
            Toast.makeText(this, "Wallet", Toast.LENGTH_SHORT).show()
        }
    }
    }
}

