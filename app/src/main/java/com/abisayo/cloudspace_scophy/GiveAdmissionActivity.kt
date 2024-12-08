package com.abisayo.cloudspace_scophy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GiveAdmissionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var applicantsList: MutableList<Applicant>
    private lateinit var adapter: ApplicantsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_give_admission)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        applicantsList = mutableListOf()
        adapter = ApplicantsAdapter(applicantsList, this)
        recyclerView.adapter = adapter

        val database = FirebaseDatabase.getInstance().reference.child("biometrics")

        // Fetching all the user IDs from the "biometrics" node
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Each child node is a user ID, we fetch the fullName under that user ID
                val userId = snapshot.key // This is the user ID (child node name)
                val fullName = snapshot.child("fullName").getValue(String::class.java)

                if (fullName != null) {
                    // Add the applicant data to the list
                    val applicant = Applicant(fullName)
                    applicantsList.add(applicant)
                    adapter.notifyDataSetChanged() // Notify the adapter to refresh the data
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
