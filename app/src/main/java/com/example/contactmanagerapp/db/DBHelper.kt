package com.example.contactmanagerapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactmanagerapp.model.Contact

class DBHelper(context: Context) : SQLiteOpenHelper(context, "contacts.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE contacts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                phone TEXT,
                email TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS contacts")
        onCreate(db)
    }

    fun addContact(contact: Contact): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", contact.name)
            put("phone", contact.phone)
            put("email", contact.email)
        }
        return db.insert("contacts", null, values) > 0
    }

    fun getAllContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM contacts", null)
        if (cursor.moveToFirst()) {
            do {
                contactList.add(
                    Contact(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        phone = cursor.getString(2),
                        email = cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }

    fun updateContact(contact: Contact): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", contact.name)
            put("phone", contact.phone)
            put("email", contact.email)
        }
        return db.update("contacts", values, "id=?", arrayOf(contact.id.toString())) > 0
    }

    fun deleteContact(id: Int): Boolean {
        return writableDatabase.delete("contacts", "id=?", arrayOf(id.toString())) > 0
    }
}