package com.example.poepart2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyRankingActivity : AppCompatActivity() {

    private lateinit var percentageText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var currentSaving: TextView
    private lateinit var rankLabel: TextView
    private lateinit var rankName: TextView
    private lateinit var seeMilestone: TextView

    private lateinit var blockBronze: View
    private lateinit var blockSilver: View
    private lateinit var blockGold: View
    private lateinit var blockPlatinum: View
    private lateinit var arrowAfterBronze: ImageView
    private lateinit var arrowAfterSilver: ImageView
    private lateinit var arrowAfterGold: ImageView
    private lateinit var nextMilestoneLabel: TextView

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rankings)

        percentageText = findViewById(R.id.percentage_text)
        progressBar = findViewById(R.id.progress_bar)
        currentSaving = findViewById(R.id.current_saving)
        rankLabel = findViewById(R.id.rank_label)
        rankName = findViewById(R.id.rank_name)
        seeMilestone = findViewById(R.id.SeeMilestone)

        blockBronze = findViewById(R.id.block_bronze)
        blockSilver = findViewById(R.id.block_silver)
        blockGold = findViewById(R.id.block_gold)
        blockPlatinum = findViewById(R.id.block_platinum)

        arrowAfterBronze = findViewById(R.id.arrow_after_bronze)
        arrowAfterSilver = findViewById(R.id.arrow_after_silver)
        arrowAfterGold = findViewById(R.id.arrow_after_gold)
        nextMilestoneLabel = findViewById(R.id.next_milestone_label)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            finish()
        }

        seeMilestone.setOnClickListener {
            val intent = Intent(this, RankMilestoneActivity::class.java)
            startActivity(intent)
        }

        loadRankingData()
    }

    private fun loadRankingData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("balances").document(userId).get().addOnSuccessListener { balanceDoc ->
            val expenses = balanceDoc.getDouble("expenses") ?: 0.0

            db.collection("budget_goals").document(userId)
                .collection("goals").get().addOnSuccessListener { goalsSnapshot ->
                    val totalGoal = goalsSnapshot.documents.sumOf {
                        it.getDouble("amount") ?: 0.0
                    }.takeIf { it > 0 } ?: 1.0

                    val percentage = ((expenses / totalGoal) * 100).toInt()
                    val saving = totalGoal - expenses

                    percentageText.text = "You Are At $percentage% Of Your Expenses Goal"
                    progressBar.progress = percentage
                    currentSaving.text = "Current Saving: R${String.format("%.2f", saving)}"

                    updateRank(expenses)
                }
        }
    }

    private fun updateRank(expenses: Double) {
        blockBronze.visibility = View.GONE
        blockSilver.visibility = View.GONE
        blockGold.visibility = View.GONE
        blockPlatinum.visibility = View.GONE
        arrowAfterBronze.visibility = View.GONE
        arrowAfterSilver.visibility = View.GONE
        arrowAfterGold.visibility = View.GONE
        nextMilestoneLabel.visibility = View.GONE

        when {
            expenses in 2500.0..4999.99 -> {
                blockBronze.visibility = View.VISIBLE
                arrowAfterBronze.visibility = View.VISIBLE
                blockSilver.visibility = View.VISIBLE
                nextMilestoneLabel.visibility = View.VISIBLE
                rankName.text = "Bronze Planner"
                rankName.setTextColor(Color.parseColor("#FFA500"))
            }
            expenses in 5000.0..9999.99 -> {
                blockSilver.visibility = View.VISIBLE
                arrowAfterSilver.visibility = View.VISIBLE
                blockGold.visibility = View.VISIBLE
                nextMilestoneLabel.visibility = View.VISIBLE
                rankName.text = "Silver Planner"
                rankName.setTextColor(Color.parseColor("#C0C0C0"))
            }
            expenses in 10000.0..19999.99 -> {
                blockGold.visibility = View.VISIBLE
                arrowAfterGold.visibility = View.VISIBLE
                blockPlatinum.visibility = View.VISIBLE
                nextMilestoneLabel.visibility = View.VISIBLE
                rankName.text = "Gold Planner"
                rankName.setTextColor(Color.parseColor("#FFD700"))
            }
            expenses >= 20000.0 -> {
                blockPlatinum.visibility = View.VISIBLE
                rankName.text = "Platinum Planner"
                rankName.setTextColor(Color.parseColor("#1E7CC7"))
            }
            else -> {
                rankName.text = "No Rank Yet"
                rankName.setTextColor(Color.WHITE)
            }
        }
    }
}

