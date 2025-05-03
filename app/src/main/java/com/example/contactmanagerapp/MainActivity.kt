package com.example.contactmanagerapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactmanagerapp.adapter.ContactAdapter
import com.example.contactmanagerapp.db.DBHelper
import com.example.contactmanagerapp.ui.AddEditContactActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        contactAdapter = ContactAdapter(this, dbHelper.getAllContacts().toMutableList()) {
            val intent = Intent(this, AddEditContactActivity::class.java)
            intent.putExtra("contact", it)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = dbHelper.getAllContacts().filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                }
                contactAdapter.updateList(filtered)
                return true
            }
        })

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddEditContactActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        contactAdapter.updateList(dbHelper.getAllContacts())
    }
}