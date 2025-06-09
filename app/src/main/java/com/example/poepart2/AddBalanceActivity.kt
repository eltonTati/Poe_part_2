package com.example.poepart2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddBalanceActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var textViewBalance: TextView
    private lateinit var editTextAmount: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextLabel: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var buttonAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_balance)

        // Firebase init
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Views
        textViewBalance = findViewById(R.id.textViewBalance)
        editTextAmount = findViewById(R.id.editTextAmount)
        editTextDate = findViewById(R.id.editTextDate)
        editTextLabel = findViewById(R.id.editTextLabel)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        buttonAdd = findViewById(R.id.buttonAdd)

        // Spinner setup
        val incomeTypes = listOf("Salary", "Investment")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, incomeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter


        // Date picker
        editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this@AddBalanceActivity, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = sdf.format(selectedDate.time)
                editTextDate.setText(formattedDate)
            }, year, month, day).show()
        }

        // Load current balance
        loadUserBalance()

        // Handle Add Balance
        buttonAdd.setOnClickListener {
            val amountStr = editTextAmount.text.toString().trim()
            if (amountStr.isEmpty()) {
                editTextAmount.error = "Amount required"
                return@setOnClickListener
            }

            val amount = amountStr.toDouble()
            val incomeType = spinnerCategory.selectedItem.toString() // "Salary" or "Investment"
            val label = editTextLabel.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val userId = auth.currentUser?.uid ?: return@setOnClickListener

            val balanceRef = firestore.collection("balances").document(userId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(balanceRef)

                val currentTotal = snapshot.getDouble("total") ?: 0.0
                val newTotal = currentTotal + amount

                transaction.update(balanceRef, "total", newTotal)
            }.addOnSuccessListener {
                Toast.makeText(this, "Wallet balance added!", Toast.LENGTH_SHORT).show()
                loadUserBalance()

                // Optionally clear fields
                editTextAmount.text.clear()
                editTextDate.text.clear()
                editTextLabel.text.clear()
                spinnerCategory.setSelection(0)
                val transactionData = hashMapOf(
                    "amount" to amount,
                    "type" to incomeType,
                    "label" to label,
                    "date" to date,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("balances").document(userId)
                    .collection("transactions")
                    .add(transactionData)
                    .addOnSuccessListener {
                        // Optional: Show success message or log
                        Toast.makeText(this, "Transaction saved!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save transaction", Toast.LENGTH_SHORT).show()
                    }

            }.addOnFailureListener {
                Toast.makeText(this, "Failed to add to wallet", Toast.LENGTH_SHORT).show()
            }
        }
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            val intent = Intent(this, WalletScreenActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loadUserBalance() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("balances").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val balance = document.getDouble("total") ?: 0.0
                textViewBalance.text = "R${"%.2f".format(balance)}"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load balance", Toast.LENGTH_SHORT).show()
            }
    }

}
