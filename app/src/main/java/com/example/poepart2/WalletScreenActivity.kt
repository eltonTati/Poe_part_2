package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WalletScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var balanceTextView: TextView
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_screen)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        balanceTextView = findViewById(R.id.textView9) // Shows "R9876" in your XML
        addButton = findViewById(R.id.add_wallet_balance_button)

        loadWalletBalance()

        addButton.setOnClickListener {
            val intent = Intent(this, AddBalanceActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh balance when returning to this screen
        loadWalletBalance()
    }

    private fun loadWalletBalance() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("balances").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val balance = document.getDouble("total") ?: 0.0
                balanceTextView.text = "R${"%.2f".format(balance)}"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load wallet balance", Toast.LENGTH_SHORT).show()
            }
    }

}