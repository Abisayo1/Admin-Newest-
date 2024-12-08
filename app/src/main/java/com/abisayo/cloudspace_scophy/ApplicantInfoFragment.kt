package com.abisayo.cloudspace_scophy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ApplicantInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApplicantInfoFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_applicant_info, container, false)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val nameTextView = view.findViewById<TextView>(R.id.display_user_name)
        val dobTextView = view.findViewById<TextView>(R.id.display_user_dob)
        val genderTextView = view.findViewById<TextView>(R.id.display_user_gender)
        val jambRegTextView = view.findViewById<TextView>(R.id.display_jamb_reg)
        val waecRegTextView = view.findViewById<TextView>(R.id.display_waec_reg)
        val jambScoreTextView = view.findViewById<TextView>(R.id.display_jamb_score)
        val jambCombinationTextView = view.findViewById<TextView>(R.id.display_jamb_combination)
        val waecCombinationTextView = view.findViewById<TextView>(R.id.display_waec_combination)

        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("biometrics").child("$userId").get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.child("fullName").value as? String ?: "Not Available"
                    val dob = snapshot.child("dateOfBirth").value as? String ?: "Not Available"
                    val gender = snapshot.child("gender").value as? String ?: "Not Available"
                    val jambReg = snapshot.child("jambRegNo").value as? String ?: "Not Available"
                    val waecReg = snapshot.child("waecRegNo").value as? String ?: "Not Available"
                    val jambScore = snapshot.child("utmeScore").value as? String ?: "Not Available"


                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val database = FirebaseDatabase.getInstance().reference

                        // Reference to WAEC and JAMB_Subjects nodes
                        val waecRef = database.child("WAEC").child(userId)
                        val jambRef = database.child("JAMB_Subjects").child(userId)

                        // Retrieve WAEC data
                        waecRef.get().addOnSuccessListener { waecSnapshot ->
                            if (waecSnapshot.exists()) {
                                // Retrieve WAEC subjects and grades
                                val waecSubjects = waecSnapshot.children.mapNotNull {
                                    "${it.key}: ${it.value}"
                                }.joinToString("\n")
                                waecCombinationTextView.text = "WAEC Subjects and Grades:\n$waecSubjects"



                            } else {

                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to fetch WAEC data: ${it.message}", Toast.LENGTH_SHORT).show()
                        }

                        // Retrieve JAMB data
                        jambRef.get().addOnSuccessListener { jambSnapshot ->
                            if (jambSnapshot.exists()) {
                                // Retrieve JAMB subjects
                                val jambSubjects = jambSnapshot.children.mapNotNull {
                                    it.value as? String
                                }.joinToString(", ")
                                jambCombinationTextView.text = "JAMB Subjects: $jambSubjects"


                                // Display JAMB subjects
                            } else {

                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to fetch JAMB data: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "User not authenticated.", Toast.LENGTH_SHORT).show()
                    }


                    // Set data to TextViews
                    nameTextView.text = "Name: $name"
                    dobTextView.text = "Date of Birth: $dob"
                    genderTextView.text = "Gender: $gender"
                    jambRegTextView.text = "JAMB Registration Number: $jambReg"
                    waecRegTextView.text = "WAEC Registration Number: $waecReg"
                    jambScoreTextView.text = "JAMB Score: $jambScore"



                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load data.", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}
