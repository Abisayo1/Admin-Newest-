package com.abisayo.cloudspace_scophy

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ApplicantsInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applicants_info)

        val sharedPreferences = getSharedPreferences("applicant_data", Context.MODE_PRIVATE)
        val applicantName = sharedPreferences.getString("applicant_name", "No name")
        val applicantId = sharedPreferences.getString("applicant_id", "No ID")

        val nameTextView: TextView = findViewById(R.id.applicantNameTextView)
        val idTextView: TextView = findViewById(R.id.applicantIdTextView)

        nameTextView.text = applicantName
        idTextView.text = applicantId
    }
}
