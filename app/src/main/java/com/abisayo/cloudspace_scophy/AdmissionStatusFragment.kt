package com.abisayo.cloudspace_scophy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdmissionStatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdmissionStatusFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admission_status, container, false)
        val statusText = view.findViewById<TextView>(R.id.admission_status_text)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("AdmissionStatus").child(userId).get()
                .addOnSuccessListener { snapshot ->
                    val status = snapshot.value as? String ?: "Not Available"
                    statusText.text = "Your Admission Status: $status"
                }
                .addOnFailureListener {
                    statusText.text = "Failed to load admission status."
                }
        }

        return view
    }
}
