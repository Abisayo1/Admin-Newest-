package com.abisayo.cloudspace_scophy

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class DetailActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var biometricsRef: DatabaseReference
    private lateinit var waecRef: DatabaseReference
    private lateinit var jambSubjectsRef: DatabaseReference

    private lateinit var spinnerAdmissionStatus: Spinner
    private lateinit var btnSubmitStatus: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        spinnerAdmissionStatus = findViewById(R.id.spinnerAdmissionStatus)
        btnSubmitStatus = findViewById(R.id.btnSubmitStatus)

        databaseReference = FirebaseDatabase.getInstance().getReference("AdmissionStatus")

        val admissionStatusOptions = listOf(
            "Select an option",
            "Congratulations! You have been given Admission; please log into your portal to accept or reject",
            "Disqualified, wrong JAMB Combination",
            "Disqualified, wrong WAEC Combination",
            "Disqualified, you did not reach the cut-off point"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, admissionStatusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdmissionStatus.adapter = adapter

        // Firebase database initialization
        database = FirebaseDatabase.getInstance()

        val userId = intent.getStringExtra("ID") ?: return

        // Handle Submit Button Click
        btnSubmitStatus.setOnClickListener {
            val selectedStatus = spinnerAdmissionStatus.selectedItem.toString()

            if (selectedStatus == "Select an option") {
                Toast.makeText(this, "Please select a valid status", Toast.LENGTH_SHORT).show()
            } else {
                submitAdmissionStatus(selectedStatus, userId)
            }
        }

        // Initialize database references
        biometricsRef = database.getReference("biometrics").child(userId)
        waecRef = database.getReference("WAEC").child(userId)
        jambSubjectsRef = database.getReference("JAMB_Subjects").child(userId)

        // TextViews
        val tvJambRegNo: TextView = findViewById(R.id.tvJambRegNo)
        val tvWaecRegNo: TextView = findViewById(R.id.tvWaecRegNo)
        val tvWaecScratchCardNo: TextView = findViewById(R.id.tvWaecScratchCardNo)
        val tvUtmeScore: TextView = findViewById(R.id.tvUtmeScore)

        val tvWaecSubjects: TextView = findViewById(R.id.tvWaecSubjects)
        val tvJambSubjects: TextView = findViewById(R.id.tvJambSubjects)

        // Fetch data from "biometrics" node
        biometricsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvJambRegNo.text = "Jamb Reg No: ${snapshot.child("jambRegNo").value.toString()}"
                tvWaecRegNo.text = "Waec Reg No: ${snapshot.child("waecRegNo").value.toString()}"
                tvWaecScratchCardNo.text = "Waec Scratch Card No: ${snapshot.child("waecScratchCardNo").value.toString()}"
                tvUtmeScore.text = "UTME Score: ${snapshot.child("utmeScore").value.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        // Fetch data from "WAEC" node
        waecRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val waecSubjects = StringBuilder()
                snapshot.children.forEach { subject ->
                    val subjectName = subject.key
                    val grade = subject.value
                    waecSubjects.append("$subjectName: $grade\n")
                }
                tvWaecSubjects.text = waecSubjects.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        // Fetch data from "JAMB_Subjects" node
        jambSubjectsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jambSubjects = StringBuilder()
                snapshot.children.forEach { subject ->
                    jambSubjects.append("${subject.value}\n")
                }
                tvJambSubjects.text = jambSubjects.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun submitAdmissionStatus(status: String, userId: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("AdmissionStatus").child(userId)
        ref.setValue(status).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Data submitted successfully", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Failed to submit data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
