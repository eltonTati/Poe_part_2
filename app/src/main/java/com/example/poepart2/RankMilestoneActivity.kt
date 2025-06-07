package com.example.poepart2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class RankMilestoneActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank_milestone)

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish()
        }
    }
}
/*
  This project includes assistance from OpenAI's ChatGPT (GPT-4) for certain sections of code and layout design.
  ChatGPT was used to clarify Kotlin syntax, improve layout structure, and implement logic such as currency conversion.
  Final code was reviewed, tested, and modified independently by the developer.

  References:
  OpenAI, 2024. ChatGPT (GPT-4) [online]. Available at: https://chat.openai.com/ [Accessed  27 April 2025].
  W3Schools, 2025. Kotlin Tutorial [online]. Available at: https://www.w3schools.com/KOTLIN/index.php [Accessed 27 april 2025].
*/