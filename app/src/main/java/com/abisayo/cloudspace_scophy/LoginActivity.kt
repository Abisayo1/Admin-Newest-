package com.abisayo.cloudspace_scophy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etName = findViewById<EditText>(R.id.etName)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val name = etName.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate password
            if (password == "GainAdmission2%25") {
                // Navigate to Manage Candidate Activity
                val intent = Intent(this, GiveAdmissionActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Show error message
                Toast.makeText(this, "Incorrect password, try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
