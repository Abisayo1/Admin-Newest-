package com.abisayo.cloudspace_scophy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApplicantsAdapter(private val applicants: List<Applicant>, private val context: Context) :
    RecyclerView.Adapter<ApplicantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_applicant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val applicant = applicants[position]


        // Set OnClickListener for the card
        holder.itemView.setOnClickListener {
            val sharedPreferences = context.getSharedPreferences("applicant_data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.apply()

            // Start new activity
            val intent = Intent(context, ApplicantsInfoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return applicants.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}
