package com.example.contactmanagerapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactmanagerapp.R
import com.example.contactmanagerapp.db.DBHelper
import com.example.contactmanagerapp.model.Contact

class AddEditContactActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private var contact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_contact)

        dbHelper = DBHelper(this)
        contact = intent.getSerializableExtra("contact") as? Contact

        val etName = findViewById<EditText>(R.id.etName)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSave = findViewById<Button>(R.id.btnSave)

        contact?.let {
            etName.setText(it.name)
            etPhone.setText(it.phone)
            etEmail.setText(it.email)
            btnSave.text = "Update"
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            val email = etEmail.text.toString()

            if (name.isBlank() || phone.isBlank() || email.isBlank()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = if (contact == null) {
                dbHelper.addContact(Contact(name = name, phone = phone, email = email))
            } else {
                dbHelper.updateContact(contact!!.apply {
                    this.name = name
                    this.phone = phone
                    this.email = email
                })
            }

            if (result) finish() else Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}