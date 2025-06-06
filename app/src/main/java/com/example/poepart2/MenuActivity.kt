package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.LinearLayout


class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var welcomeText: TextView
    private lateinit var totalBalanceText: TextView
    private lateinit var expensesText: TextView
    private lateinit var actualBalanceText: TextView

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        drawerLayout = findViewById(R.id.main_drawer)
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        welcomeText = findViewById(R.id.welcome_text)
        val balanceSection = findViewById<LinearLayout>(R.id.balance_section)

        val totalBalanceText = balanceSection.findViewWithTag<TextView>("Total Balance")
        val expensesText = balanceSection.findViewWithTag<TextView>("Expenses")
        val actualBalanceText = balanceSection.findViewWithTag<TextView>("Actual Balance")

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Fetch user name
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val name = document.getString("name") ?: "User"
                    welcomeText.text = "Hi $name,\nWelcome back"
                }

            // Fetch Balance info
            db.collection("balances").document(userId).get()
                .addOnSuccessListener { doc ->
                    val total = doc.getDouble("total") ?: 0.0
                    totalBalanceText.text = "Total Balance\nR $total"

                    val expenses = doc.getDouble("expenses") ?: 0.0
                    expensesText.text = "Expenses\nR $expenses"

                    val actual = total - expenses
                    actualBalanceText.text = "Actual Balance\nR $actual"
                }
                .addOnFailureListener {
                    totalBalanceText.text = "Total Balance\nN/A"
                    expensesText.text = "Expenses\nN/A"
                    actualBalanceText.text = "Actual Balance\nN/A"
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
}
