package com.example.poepart2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AnalyticsScreenActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var barChart: BarChart
    private lateinit var infoContainer: LinearLayout
    private lateinit var tabGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_screen)

        barChart = findViewById(R.id.barChart)
        infoContainer =findViewById(R.id.infoBoxes)
        tabGroup = findViewById(R.id.tabGroup)

        firestore = FirebaseFirestore.getInstance()

//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        if (userId != null) {
//            loadBudgetGoals(userId)
//        }


        tabGroup.setOnCheckedChangeListener { _, checkedId ->
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnCheckedChangeListener

            val recurrence = when (checkedId) {
                R.id.rbDay -> "Daily"
                R.id.rbMonth -> "Monthly"
                R.id.rbYear -> "Yearly"
                else -> "Monthly"
            }

            loadBudgetGoals(userId, recurrence)
        }

        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<ImageView>(R.id.navAnalysis).setOnClickListener {
            startActivity(Intent(this, AnalyticsScreenActivity::class.java))
        }
        findViewById<ImageView>(R.id.navTransactions).setOnClickListener {
            startActivity(Intent(this, WalletScreenActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, UserScreenActivity::class.java))
        }
    }




    private fun loadBudgetGoals(userId: String, recurrenceFilter: String) {
        val goalsRef = firestore.collection("budget_goals")
            .document(userId)
            .collection("goals")
            .whereEqualTo("recurrence", recurrenceFilter)

        goalsRef.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                infoContainer.removeAllViews()
                val entries = mutableListOf<BarEntry>()
                val labels = mutableListOf<String>()

                var index = 0f

                for (doc in querySnapshot.documents) {
                    val budgetName = doc.getString("budgetName") ?: "Unknown"
                    val amount = doc.getDouble("amount")?.toFloat() ?: 0f

                    // Add info box
                    addInfoBox(budgetName, "R${amount.toInt()}")

                    // Add chart entry
                    entries.add(BarEntry(index, amount))
                    labels.add(budgetName)
                    index += 1f
                }

                setupBarChart(entries, labels)
            } else {
                infoContainer.removeAllViews()
                barChart.clear()
                Toast.makeText(this, "No $recurrenceFilter budget goals found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error loading goals", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addInfoBox(label: String, value: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_info_box, infoContainer, false)
        view.findViewById<TextView>(R.id.tvLabel).text = "$label:"
        view.findViewById<TextView>(R.id.tvAmount).text = value
        view.findViewById<TextView>(R.id.tvPercent).text = ""
        infoContainer.addView(view)
    }

    private fun setupBarChart(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Budget Goals")
        dataSet.color = Color.CYAN
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barData.barWidth = 0.4f

        barChart.data = barData

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.WHITE
            granularity = 1f
            setDrawGridLines(false)
        }

        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.invalidate()
    }
}


