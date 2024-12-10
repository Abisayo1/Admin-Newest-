
package com.abisayo.cloudspace_scophy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApplicantsAdapter(private val userList : ArrayList<Applicant>) : RecyclerView.Adapter<ApplicantsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_applicant,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]

        holder.firstName.text = currentitem.fullName
        holder.lastName.text = currentitem.gender
        holder.id.text = currentitem.id

        // Set an OnClickListener for the item
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("FULL_NAME", currentitem.fullName)
                putExtra("GENDER", currentitem.gender)
                putExtra("ID", currentitem.id)
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val firstName : TextView = itemView.findViewById(R.id.tvfullName)
        val lastName : TextView = itemView.findViewById(R.id.tvgender)
        val id : TextView = itemView.findViewById(R.id.tvid)

    }

}
