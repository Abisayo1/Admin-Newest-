package com.abisayo.cloudspace_scophy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Biometrics : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.biometrics, container, false)

        // Initialize Views
        val fullName = view.findViewById<EditText>(R.id.full_name)
        val dateOfBirth = view.findViewById<EditText>(R.id.date_of_birth)
        val genderGroup = view.findViewById<RadioGroup>(R.id.gender_group)
        val utmeScore = view.findViewById<EditText>(R.id.utme_score)
        val jambRegNo = view.findViewById<EditText>(R.id.jamb_reg_no)
        val waecRegNo = view.findViewById<EditText>(R.id.waec_reg_no)
        val waecScratchCardNo = view.findViewById<EditText>(R.id.waec_scratch_card_no)
        val submitButton = view.findViewById<Button>(R.id.submit_button)

        // Set Click Listener for Submit Button
        submitButton.setOnClickListener {
            val selectedGenderId = genderGroup.checkedRadioButtonId
            val selectedGender = if (selectedGenderId != -1) {
                view.findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                null
            }

            // Validate inputs
            if (fullName.text.isNullOrEmpty() || dateOfBirth.text.isNullOrEmpty() ||
                utmeScore.text.isNullOrEmpty() || jambRegNo.text.isNullOrEmpty() ||
                waecRegNo.text.isNullOrEmpty() || waecScratchCardNo.text.isNullOrEmpty() ||
                selectedGender == null) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get Data
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid // Replace with actual user ID
            val data = mapOf(
                "fullName" to fullName.text.toString(),
                "dateOfBirth" to dateOfBirth.text.toString(),
                "gender" to selectedGender,
                "utmeScore" to utmeScore.text.toString(),
                "jambRegNo" to jambRegNo.text.toString(),
                "waecRegNo" to waecRegNo.text.toString(),
                "waecScratchCardNo" to waecScratchCardNo.text.toString(),
                "id" to userId
            )

            // Send Data to Firebase
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("biometrics").child("$userId")
            ref.setValue(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Data submitted successfully", Toast.LENGTH_SHORT).show()
                    fullName.text.clear()
                    dateOfBirth.text.clear()
                    utmeScore.text.clear()
                    jambRegNo.text.clear()
                    waecRegNo.text.clear()
                    waecScratchCardNo.text.clear()
                    genderGroup.clearCheck() // Clear selected radio button
                } else {
                    Toast.makeText(context, "Failed to submit data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
