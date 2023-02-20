package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class History : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var name_for_history : TextView
    private lateinit var weight_for_history : TextView
    private lateinit var bmi_for_history : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val nameEdittext = findViewById<EditText>(R.id.getName)
        val searchButton = findViewById<Button>(R.id.searchDatabase)
        name_for_history= findViewById(R.id.name_for_history)
        weight_for_history = findViewById(R.id.weight_for_history)
        bmi_for_history = findViewById(R.id.bmi_for_history)
        searchButton.setOnClickListener{
            val searchKey = nameEdittext.text.toString().trim()
            if(searchKey.isNotEmpty()){
                readData(searchKey)
            }
            else{
                Toast.makeText(this,"Enter Valid Username",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun readData(searchKey:String) {
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("user")
        databaseReference.child(searchKey).get().addOnSuccessListener{
            if (it.exists()){
                val name = it.child("name").value
                val weight = it.child("weight").value
                val bmi = it.child("bmi").value
                val formattedBMI = String.format("%.2f", bmi)

                name_for_history.text = name.toString()
                weight_for_history.text = weight.toString()
                bmi_for_history.text = formattedBMI
            }
            else{
                Toast.makeText(this,"Enter Valid Username",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
