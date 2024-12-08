package com.abisayo.cloudspace_scophy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abisayo.cloudspace_scophy.databinding.ActivityAdmissionStatusBinding


class AdmissionStatusActivity : AppCompatActivity() {
    lateinit var binding : ActivityAdmissionStatusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmissionStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the first fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ApplicantInfoFragment())
            .commit()
        binding.showStatus.setOnClickListener {
            showAdmissionStatus()
        }

        binding.bio.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ApplicantInfoFragment())
                .commit()
        }

    }

    // Navigate to Admission Status Fragment
    fun showAdmissionStatus() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AdmissionStatusFragment())
            .addToBackStack(null)
            .commit()
    }
}
