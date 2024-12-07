package com.abisayo.cloudspace_scophy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EnterWACESubjects : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.waec_combination, container, false)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Compulsory subjects
        val gradeEnglishSpinner = view.findViewById<Spinner>(R.id.grade_english)
        val gradeMathSpinner = view.findViewById<Spinner>(R.id.grade_math)

        // Elective subjects
        val subjects = listOf(
            Pair(view.findViewById<CheckBox>(R.id.subject_physics), view.findViewById<Spinner>(R.id.grade_physics)),
            Pair(view.findViewById<CheckBox>(R.id.subject_biology), view.findViewById<Spinner>(R.id.grade_biology)),
            Pair(view.findViewById<CheckBox>(R.id.subject_further_mathematics), view.findViewById<Spinner>(R.id.grade_further_mathematics)),
            Pair(view.findViewById<CheckBox>(R.id.subject_economics), view.findViewById<Spinner>(R.id.grade_economics)),
            Pair(view.findViewById<CheckBox>(R.id.subject_literature), view.findViewById<Spinner>(R.id.grade_literature)),
            Pair(view.findViewById<CheckBox>(R.id.subject_government), view.findViewById<Spinner>(R.id.grade_government)),
            Pair(view.findViewById<CheckBox>(R.id.subject_chemistry), view.findViewById<Spinner>(R.id.grade_chemistry)),
            Pair(view.findViewById<CheckBox>(R.id.subject_accounting), view.findViewById<Spinner>(R.id.grade_accounting)),
            Pair(view.findViewById<CheckBox>(R.id.subject_commerce), view.findViewById<Spinner>(R.id.grade_commerce)),
            Pair(view.findViewById<CheckBox>(R.id.subject_agricultural_science), view.findViewById<Spinner>(R.id.grade_agricultural_science)),
            Pair(view.findViewById<CheckBox>(R.id.subject_food_nutrition), view.findViewById<Spinner>(R.id.grade_food_nutrition)),
            Pair(view.findViewById<CheckBox>(R.id.subject_home_economics), view.findViewById<Spinner>(R.id.grade_home_economics)),
            Pair(view.findViewById<CheckBox>(R.id.subject_arabic), view.findViewById<Spinner>(R.id.grade_arabic))
        )

        // Grade adapter
        val gradesArray = resources.getStringArray(R.array.waec_grades)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gradesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gradeEnglishSpinner.adapter = adapter
        gradeMathSpinner.adapter = adapter
        subjects.forEach { (_, spinner) -> spinner.adapter = adapter }

        // Show or hide spinners based on checkbox selection
        subjects.forEach { (checkBox, spinner) ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                spinner.visibility = if (isChecked) View.VISIBLE else View.GONE
            }
        }

        // Handle Submit Button
        view.findViewById<Button>(R.id.submit_button).setOnClickListener {
            val selectedSubjects = mutableListOf<Pair<String, String>>()

            // Add compulsory subjects
            selectedSubjects.add("English Language" to gradeEnglishSpinner.selectedItem.toString())
            selectedSubjects.add("Mathematics" to gradeMathSpinner.selectedItem.toString())

            // Add elective subjects if checked
            subjects.forEach { (checkBox, spinner) ->
                if (checkBox.isChecked) {
                    selectedSubjects.add(checkBox.text.toString() to spinner.selectedItem.toString())
                }
            }

            // Validate subject count
            if (selectedSubjects.size != 9) {
                Toast.makeText(requireContext(), "Please select exactly 9 subjects", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Get user ID and save data to Firebase
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val waecCombinations = selectedSubjects.associate { it.first to it.second }
                database.child(userId).child("WAEC Combinations").setValue(waecCombinations)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "WAEC Combinations saved successfully!", Toast.LENGTH_SHORT).show()
                        clearSelections(subjects, gradeEnglishSpinner, gradeMathSpinner)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to save WAEC Combinations.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "User not authenticated. Please log in.", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun clearSelections(
        subjects: List<Pair<CheckBox, Spinner>>,
        gradeEnglishSpinner: Spinner,
        gradeMathSpinner: Spinner
    ) {
        // Reset checkboxes and spinners for elective subjects
        subjects.forEach { (checkBox, spinner) ->
            checkBox.isChecked = false
            spinner.visibility = View.GONE
            spinner.setSelection(0) // Reset to default grade
        }

        // Reset compulsory subjects spinners
        gradeEnglishSpinner.setSelection(0)
        gradeMathSpinner.setSelection(0)
    }
}
