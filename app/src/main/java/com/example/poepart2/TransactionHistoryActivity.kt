package com.example.poepart2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.Query

class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerFilter: Spinner
    private lateinit var adapter: TransactionAdapter
    private val transactionList = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        val buttonAdd: Button = findViewById(R.id.buttonAdd)

        buttonAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
        val backToWalletBtn: ImageView = findViewById(R.id.backToWalletBtn)

        backToWalletBtn.setOnClickListener {
            val intent = Intent(this, WalletScreenActivity::class.java)
            startActivity(intent)
            finish() // Optional: close the current screen
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerTransactions)
        spinnerFilter = findViewById(R.id.spinnerFilter)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(transactionList)
        recyclerView.adapter = adapter

        // Spinner setup
        val filters = listOf("All", "Recent")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filters)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = filterAdapter

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val filter = filters[position]
                loadTransactions(filter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadTransactions(filter: String) {
        val userId = auth.currentUser?.uid ?: return
        val transactionsRef = firestore.collection("transactions")

        var query = transactionsRef
            .whereEqualTo("userId", userId) // très important si tu veux filtrer par utilisateur
            .orderBy("timestamp", Query.Direction.DESCENDING)

        if (filter == "Recent") {
            val oneWeekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
            query = query.whereGreaterThanOrEqualTo("timestamp", oneWeekAgo)
        }

        query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Transaction", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            transactionList.clear()

            if (snapshot != null && !snapshot.isEmpty) {
                for (document in snapshot.documents) {
                    val name = document.getString("name") ?: "N/A"
                    val dateTimestamp = document.getLong("timestamp") ?: 0
                    val amount = document.getDouble("amount") ?: 0.0
                    val category = document.getString("category") ?: "Expense"

                    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateTimestamp))

                    transactionList.add(Transaction(name, formattedDate, amount, category))
                }
            } else {
                // Si la collection est vide
                transactionList.add(Transaction("No transactions yet", "", 0.0, "Info"))
            }

            adapter.notifyDataSetChanged()
        }
    }

    data class Transaction(
        val name: String,
        val date: String,
        val amount: Double,
        val category: String // "Income" or "Expense"
    )

    class TransactionAdapter(private val transactions: List<Transaction>) :
        RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

        inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textName: TextView = itemView.findViewById(R.id.textLabel)
            val textDate: TextView = itemView.findViewById(R.id.textDate)
            val textAmount: TextView = itemView.findViewById(R.id.textAmount)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): TransactionViewHolder {
            val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
            return TransactionViewHolder(view)
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            val transaction = transactions[position]

            holder.textName.text = transaction.name
            holder.textDate.text = transaction.date

            if (transaction.category == "Income") {
                holder.textAmount.setTextColor(Color.parseColor("#008000")) // Green
                holder.textAmount.text = "R${"%.2f".format(transaction.amount)}"
            } else {
                holder.textAmount.setTextColor(Color.parseColor("#FF0000")) // Red
                holder.textAmount.text = "-R${"%.2f".format(transaction.amount)}"
            }
        }

        override fun getItemCount(): Int = transactions.size
        }
}