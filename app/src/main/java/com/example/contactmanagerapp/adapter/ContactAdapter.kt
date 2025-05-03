package com.example.contactmanagerapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.contactmanagerapp.R
import com.example.contactmanagerapp.db.DBHelper
import com.example.contactmanagerapp.model.Contact

class ContactAdapter(
    private val context: Context,
    private var contactList: MutableList<Contact>,
    private val listener: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val phone: TextView = view.findViewById(R.id.tvPhone)
        val email: TextView = view.findViewById(R.id.tvEmail)
        val btnCall: ImageButton = view.findViewById(R.id.btnCall)
        val btnMail: ImageButton = view.findViewById(R.id.btnMail)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val card: View = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = contactList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.name
        holder.phone.text = contact.phone
        holder.email.text = contact.email

        holder.card.setOnClickListener { listener(contact) }

        holder.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${contact.phone}")
            }
            context.startActivity(intent)
        }
        holder.btnCall.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_green_dark))
        holder.btnMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${contact.email}")
            }
            context.startActivity(intent)
        }
        holder.btnMail.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_orange_dark))
        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Delete Contact")
                setMessage("Are you sure?")
                setPositiveButton("Yes") { _, _ ->
                    val db = DBHelper(context)
                    db.deleteContact(contact.id)
                    contactList.removeAt(position)
                    notifyItemRemoved(position)
                }
                setNegativeButton("No", null)
                show()
            }
        }
    }

    fun updateList(newList: List<Contact>) {
        contactList = newList.toMutableList()
        notifyDataSetChanged()
    }
}
