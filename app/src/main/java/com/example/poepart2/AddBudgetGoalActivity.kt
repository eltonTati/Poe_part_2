package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddBudgetGoalActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var budgetNameEditText: EditText
    private lateinit var recurrenceDropdown: AutoCompleteTextView
    private lateinit var btnAdd: Button
    private lateinit var btnCancel: Button
    private lateinit var dropdownIcon: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val recurrenceOptions = listOf("Daily", "Weekly", "Monthly", "Yearly")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget_goal)

        amountEditText = findViewById(R.id.amountEditText)
        budgetNameEditText = findViewById(R.id.budgetNameEditText)
        recurrenceDropdown = findViewById(R.id.recurrenceDropdown)
        btnAdd = findViewById(R.id.btnAdd)
        btnCancel = findViewById(R.id.btnCancel)
        dropdownIcon = findViewById(R.id.dropdownIcon)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set dropdown adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, recurrenceOptions)
        recurrenceDropdown.setAdapter(adapter)

        // Open dropdown when field or icon is clicked
        recurrenceDropdown.setOnClickListener {
            recurrenceDropdown.showDropDown()
        }

        dropdownIcon.setOnClickListener {
            recurrenceDropdown.showDropDown()
        }

        btnAdd.setOnClickListener { saveBudgetGoal() }
        btnCancel.setOnClickListener { finish() }
    }

    private fun saveBudgetGoal() {
        val amountStr = amountEditText.text.toString().trim()
        val budgetName = budgetNameEditText.text.toString().trim()
        val recurrence = recurrenceDropdown.text.toString().trim()

        if (amountStr.isEmpty() || budgetName.isEmpty() || recurrence.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            amountEditText.error = "Enter a valid amount"
            return
        }

        val userId = auth.currentUser?.uid ?: return
        val budgetGoal = hashMapOf(
            "amount" to amount,
            "budgetName" to budgetName,
            "recurrence" to recurrence
        )

        firestore.collection("budget_goals")
            .document(userId)
            .collection("goals")
            .add(budgetGoal)
            .addOnSuccessListener {
                Toast.makeText(this, "Budget goal saved", Toast.LENGTH_SHORT).show()
                // Navigate back to BudgetGoalsActivity
                val intent = Intent(this, BudgetGoalsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
