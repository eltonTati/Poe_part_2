package com.example.poepart2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var languageSpinner: Spinner
    private lateinit var currentLanguageText: TextView

    private val languages = arrayOf("English", "Afrikaans", "French", "Spanish", "Portuguese")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        languageSpinner = findViewById(R.id.language_spinner)
        currentLanguageText = findViewById(R.id.current_language)

        val adapter = ArrayAdapter(this, R.layout.spinner_text, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                currentLanguageText.text = "Current language: $selectedLanguage"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}
