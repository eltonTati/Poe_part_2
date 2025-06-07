package com.example.poepart2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class CurrencySettingsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_settings)

        val backArrow = findViewById<ImageView>(R.id.backArrow)
        val convertButton = findViewById<Button>(R.id.convertbtn)
        val etRandInput = findViewById<EditText>(R.id.etRandInput)
        val tvDollarOutput = findViewById<TextView>(R.id.tvDollarOutput)


        backArrow.setOnClickListener {
            finish()
        }


        convertButton.setOnClickListener {
            val randValue = etRandInput.text.toString().toDoubleOrNull()

            if (randValue != null) {
                val dollarValue = randValue * 0.054
                tvDollarOutput.text = "$" + String.format("%.2f", dollarValue)
            } else {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
/*
  This project includes assistance from OpenAI's ChatGPT (GPT-4) for certain sections of code and layout design.
  ChatGPT was used to clarify Kotlin syntax, improve layout structure, and implement logic such as currency conversion.
  Final code was reviewed, tested, and modified independently by the developer.

  References:
  OpenAI, 2024. ChatGPT (GPT-4) [online]. Available at: https://chat.openai.com/ [Accessed  27 April 2025].
  W3Schools, 2025. Kotlin Tutorial [online]. Available at: https://www.w3schools.com/KOTLIN/index.php [Accessed 2 april 2025].
*/
