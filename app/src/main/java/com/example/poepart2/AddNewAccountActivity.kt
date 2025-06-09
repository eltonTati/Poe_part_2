package com.example.poepart2
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddNewAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        val accountNameInput = findViewById<EditText>(R.id.accountNameInput)
        val accountNumberInput = findViewById<EditText>(R.id.accountNumberInput)
        val accountTypeInput = findViewById<EditText>(R.id.accountTypeInput)

        val addButton = findViewById<Button>(R.id.addButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)


        addButton.setOnClickListener {
            val name = accountNameInput.text.toString().trim()
            val number = accountNumberInput.text.toString().trim()
            val type = accountTypeInput.text.toString().trim()


            if (name.isNotEmpty() && number.isNotEmpty() && type.isNotEmpty()) {
                Toast.makeText(this, "Comming soon!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }


        cancelButton.setOnClickListener {
            finish()
        }
    }
}
