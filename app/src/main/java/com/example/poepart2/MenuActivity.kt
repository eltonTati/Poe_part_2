package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var welcomeText: TextView
    private lateinit var totalBalanceText: TextView
    private lateinit var expensesText: TextView
    private lateinit var actualBalanceText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var mostExpenseText: TextView
    private lateinit var mostExpenseIcon: ImageView
    private lateinit var others: TextView
    private lateinit var othersIcon: ImageView
    private lateinit var otherExpensesList: LinearLayout

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var balanceListener: ListenerRegistration? = null
    private var userListener: ListenerRegistration? = null
    private var expensesListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        drawerLayout = findViewById(R.id.main_drawer)
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        welcomeText = findViewById(R.id.welcome_text)
        progressBar = findViewById(R.id.progress_bar)
        mostExpenseText = findViewById(R.id.most_expense_text)
        othersIcon = findViewById(R.id.othersIcons)
        others = findViewById(R.id.others)
        mostExpenseIcon = findViewById(R.id.most_expense_icon)
        otherExpensesList = findViewById(R.id.others_expense_list)

        val balanceSection = findViewById<LinearLayout>(R.id.balance_section)
        totalBalanceText = balanceSection.findViewWithTag("Total Balance")
        expensesText = balanceSection.findViewWithTag("Expenses")
        actualBalanceText = balanceSection.findViewWithTag("Actual Balance")

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Welcome text realtime
            userListener = db.collection("users").document(userId)
                .addSnapshotListener { document, _ ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "User"
                        welcomeText.text = "Hi $name,\nWelcome back"
                    }
                }

            // Balance realtime
            balanceListener = db.collection("balances").document(userId)
                .addSnapshotListener { doc, _ ->
                    if (doc != null && doc.exists()) {
                        val total = doc.getDouble("total") ?: 0.0
                        val expenses = doc.getDouble("expenses") ?: 0.0
                        val actual = total - expenses

                        totalBalanceText.text = "Total Balance\nR $total"
                        expensesText.text = "Expenses\nR $expenses"
                        actualBalanceText.text = "Actual Balance\nR $actual"

                        val percentage = if (total != 0.0) ((expenses / total) * 100).toInt() else 0
                        progressBar.progress = percentage
                    } else {
                        totalBalanceText.text = "Total Balance\nN/A"
                        expensesText.text = "Expenses\nN/A"
                        actualBalanceText.text = "Actual Balance\nN/A"
                        progressBar.progress = 0
                    }
                }

            // Expenses realtime (or default if no collection exists)
            expensesListener = db.collection("expenses")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null || snapshot == null || snapshot.isEmpty) {
                        mostExpenseText.text = "No expenses now"
                        mostExpenseIcon.setImageResource(0) // Remove image

                        others.text = "No expenses now"
                        othersIcon.setImageResource(0) // Remove image
                        return@addSnapshotListener
                    }

                    // Get highest expense
                    val expenses = snapshot.documents.mapNotNull { it.data }
                    val sortedExpenses = expenses.sortedByDescending { (it["amount"] as? Number)?.toDouble() ?: 0.0 }

                    val most = sortedExpenses.firstOrNull()
                    if (most != null) {
                        mostExpenseText.text = "${most["name"]}: ${most["amount"]} R"
                        val iconUrl = most["iconUrl"] as? String
                        if (!iconUrl.isNullOrEmpty()) {
                            Glide.with(this).load(iconUrl).into(mostExpenseIcon)
                        } else {
                            mostExpenseIcon.setImageResource(0)
                        }
                    }

                    // Display others
                    otherExpensesList.removeAllViews()
                    val others = sortedExpenses.drop(1)
                    if (others.isEmpty()) {
                        val none = TextView(this)
                        none.text = "No other expenses"
                        none.setTextColor(resources.getColor(android.R.color.darker_gray))
                        otherExpensesList.addView(none)
                    } else {
                        for (expense in others) {
                            val row = LinearLayout(this).apply {
                                orientation = LinearLayout.HORIZONTAL
                                setPadding(8, 8, 8, 8)
                            }

                            val image = ImageView(this)
                            image.layoutParams = LinearLayout.LayoutParams(40, 40)
                            val url = expense["iconUrl"] as? String
                            if (!url.isNullOrEmpty()) {
                                Glide.with(this).load(url).into(image)
                            }

                            val text = TextView(this)
                            text.text = "${expense["name"]}: ${expense["amount"]} R"
                            text.setPadding(12, 0, 0, 0)

                            row.addView(image)
                            row.addView(text)
                            otherExpensesList.addView(row)
                        }
                    }
                }

        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        findViewById<TextView>(R.id.languageText).setOnClickListener {
            startActivity(Intent(this, LanguageSelectionActivity::class.java))
        }

        findViewById<TextView>(R.id.contactSupport).setOnClickListener {
            startActivity(Intent(this, SupportActivity::class.java))
        }

        findViewById<TextView>(R.id.cat).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        findViewById<TextView>(R.id.acc).setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }

        findViewById<TextView>(R.id.curr).setOnClickListener {
            startActivity(Intent(this, CurrencySettingsActivity::class.java))
        }

        findViewById<TextView>(R.id.rank).setOnClickListener {
            startActivity(Intent(this, MyRankingActivity::class.java))
        }

        findViewById<ImageView>(R.id.navTransactions).setOnClickListener {
            startActivity(Intent(this, WalletScreenActivity::class.java))
        }

        findViewById<ImageView>(R.id.navAnalysis).setOnClickListener {
            startActivity(Intent(this, AnalyticsScreenActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, UserScreenActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        balanceListener?.remove()
        userListener?.remove()
        expensesListener?.remove()
    }
}
