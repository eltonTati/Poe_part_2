package com.example.poepart2

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var languageSpinner: Spinner
    private lateinit var currentLanguageText: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private val languages = arrayOf("English", "Afrikaans", "French", "Spanish", "Portuguese")
    private val languageCodes = arrayOf("en", "af", "fr", "es", "pt")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Load saved language preference
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val savedLanguageCode = sharedPreferences.getString("LANGUAGE_CODE", "en")
        setLocale(savedLanguageCode!!)

        setContentView(R.layout.activity_language_selection)

        languageSpinner = findViewById(R.id.language_spinner)
        currentLanguageText = findViewById(R.id.current_language)

        val adapter = ArrayAdapter(this, R.layout.spinner_text, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // Set spinner to current language
        val currentIndex = languageCodes.indexOf(savedLanguageCode)
        languageSpinner.setSelection(currentIndex)

        currentLanguageText.text = "Current language: ${languages[currentIndex]}"

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedLanguageCode = languageCodes[position]
                val selectedLanguageName = languages[position]

                // Save language choice
                val editor = sharedPreferences.edit()
                editor.putString("LANGUAGE_CODE", selectedLanguageCode)
                editor.apply()

                // Update locale
                setLocale(selectedLanguageCode)

                // Update text
                currentLanguageText.text = "Current language: $selectedLanguageName"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        backArrow.setOnClickListener {
            finish()
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)

        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics)
            }
}

