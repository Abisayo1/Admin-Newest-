package com.abisayo.cloudspace_scophy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EnterJAMBSubject : Fragment() {
    private lateinit var checkBoxes: List<CheckBox>
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.jamb_combination, container, false)

        // Initialize checkboxes
        checkBoxes = listOf(
            view.findViewById(R.id.subject_arabic),
            view.findViewById(R.id.subject_crs),
            view.findViewById(R.id.subject_english),
            view.findViewById(R.id.subject_french),
            view.findViewById(R.id.subject_geography),
            view.findViewById(R.id.subject_government),
            view.findViewById(R.id.subject_history),
            view.findViewById(R.id.subject_igbo),
            view.findViewById(R.id.subject_is),
            view.findViewById(R.id.subject_literature),
            view.findViewById(R.id.subject_music),
            view.findViewById(R.id.subject_yoruba),
            view.findViewById(R.id.subject_agriculture),
            view.findViewById(R.id.subject_biology),
            view.findViewById(R.id.subject_chemistry),
            view.findViewById(R.id.subject_physics),
            view.findViewById(R.id.subject_mathematics),
            view.findViewById(R.id.subject_accounting),
            view.findViewById(R.id.subject_business_studies),
            view.findViewById(R.id.subject_economics),
            view.findViewById(R.id.subject_computer_studies),
            view.findViewById(R.id.subject_home_economics)
        )

        // Initialize submit button
        submitButton = view.findViewById(R.id.submit_button)

        // Set up submit button click listener
        submitButton.setOnClickListener {
            handleSubmission()
        }

        return view
    }

    private fun handleSubmission() {
        // Filter selected checkboxes
        val selectedSubjects = checkBoxes.filter { it.isChecked }.map { it.text.toString() }

        // Ensure exactly 4 subjects are selected
        if (selectedSubjects.size != 4) {
            Toast.makeText(
                requireContext(),
                "Please select exactly 4 subjects.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        // Save selected subjects to Firebase
        val database = FirebaseDatabase.getInstance().reference.child("JAMB_Subjects")
        database.child("$userId").setValue(selectedSubjects).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Clear all checkboxes upon success
                checkBoxes.forEach { it.isChecked = false }
                Toast.makeText(
                    requireContext(),
                    "Subjects submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed to submit. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
