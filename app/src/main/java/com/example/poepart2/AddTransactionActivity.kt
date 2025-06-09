package com.example.poepart2
import android.content.Intent
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var amountEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var btnAdd: Button
    private lateinit var btnCancel: Button

    private val categories = listOf("Expense")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        amountEditText = findViewById(R.id.amountEditText)
        nameEditText = findViewById(R.id.nameEditText)
        dateEditText = findViewById(R.id.dateEditText)
        categoryDropdown = findViewById(R.id.categoryDropdown)
        btnAdd = findViewById(R.id.btnAdd)
        btnCancel = findViewById(R.id.btnCancel)

        // Setup category dropdown adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        categoryDropdown.setAdapter(adapter)

        // Disable keyboard input, show dropdown on click
        categoryDropdown.keyListener = null
        categoryDropdown.setOnClickListener {
            categoryDropdown.showDropDown()
        }

        // Date picker setup
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateEditText.setText(sdf.format(selectedDate.time))
            }, year, month, day).show()
        }

        btnAdd.setOnClickListener {
            addTransaction()
        }

        btnCancel.setOnClickListener {
            finish() // close activity
        }
    }

    private fun addTransaction() {
        val amountStr = amountEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val date = dateEditText.text.toString().trim()
        val category = categoryDropdown.text.toString().trim()

        if (amountStr.isEmpty()) {
            amountEditText.error = "Amount is required"
            return
        }
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            amountEditText.error = "Enter a valid amount"
            return
        }

        if (name.isEmpty()) {
            nameEditText.error = "Name is required"
            return
        }

        if (date.isEmpty()) {
            dateEditText.error = "Date is required"
            return
        }

        if (category.isEmpty() || category !in categories) {
            categoryDropdown.error = "Select category"
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val transactionData = hashMapOf(
            "amount" to amount,
            "name" to name,
            "date" to date,
            "category" to category,
            "timestamp" to System.currentTimeMillis(),
            "userId" to userId
        )

        val transactionsRef = firestore.collection("transactions")

        // 🔍 Check if the collection already contains any document (optional, but just in case you want to react to it)
        transactionsRef.add(transactionData)
            .addOnSuccessListener {
                Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show()

                // ✅ Update balance if expense
                if (category == "Expense") {
                    val balanceDocRef = firestore.collection("balances").document(userId)
                    firestore.runTransaction { transaction ->
                        val snapshot = transaction.get(balanceDocRef)
                        val currentExpense = snapshot.getDouble("expenses") ?: 0.0
                        transaction.update(balanceDocRef, "expenses", currentExpense + amount)
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to update total expense: \${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                val intent = Intent(this, TransactionHistoryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add transaction: \${e.message}", Toast.LENGTH_SHORT).show()
            }
    }




    private fun clearFields() {
        amountEditText.text.clear()
        nameEditText.text.clear()
        dateEditText.text.clear()
        categoryDropdown.text.clear()
    }
}
