package com.example.poepart2

import BudgetGoal
import BudgetGoalAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BudgetGoalsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BudgetGoalAdapter
    private val budgetGoals = mutableListOf<BudgetGoal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goals)

        val backToWalletBtn: ImageView = findViewById(R.id.backToWalletBtn)
        backToWalletBtn.setOnClickListener {
            val intent = Intent(this, WalletScreenActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btn_add_budget_goal).setOnClickListener {
            val intent = Intent(this, AddBudgetGoalActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.budgetGoalsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BudgetGoalAdapter(budgetGoals)
        recyclerView.adapter = adapter

        fetchBudgetGoalsAndTransactions()
    }

    private fun fetchBudgetGoalsAndTransactions() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        val goalsRef = db.collection("budget_goals").document(userId).collection("goals")
        val transactionsRef = db.collection("transactions").document(userId).collection("items")

        goalsRef.get().addOnSuccessListener { goalSnapshot ->
            budgetGoals.clear()
            val tempGoals = mutableListOf<BudgetGoal>()

            for (document in goalSnapshot.documents) {
                val goal = document.toObject(BudgetGoal::class.java)
                if (goal != null) {
                    tempGoals.add(goal)
                }
            }

            // Now fetch transactions and calculate spent amount
            transactionsRef.get().addOnSuccessListener { transactionSnapshot ->
                val transactions = transactionSnapshot.documents.mapNotNull { it.data }

                for (goal in tempGoals) {
                    val matchingTransactions = transactions.filter {
                        it["category"]?.toString()?.equals("Expense", ignoreCase = true) == true &&
                                it["name"]?.toString()?.trim()?.equals(goal.budgetName.trim(), ignoreCase = true) == true
                    }

                    val totalSpent = matchingTransactions.sumOf {
                        (it["amount"] as? Number)?.toDouble() ?: 0.0
                    }

                    goal.spentAmount = totalSpent
                    budgetGoals.add(goal)
                }

                adapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching transactions: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error fetching budget goals: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
