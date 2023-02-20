package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton:Button = findViewById<Button>(R.id.login_button)
        val name:EditText = findViewById<EditText>(R.id.name_edit_text)
        val age:EditText = findViewById<EditText>(R.id.age_edit_text)
        val weight:EditText = findViewById<EditText>(R.id.weight)
        val heightF:EditText = findViewById<EditText>(R.id.heightF)
        val heightI:EditText = findViewById<EditText>(R.id.heightI)
        val bmiValue:TextView = findViewById<EditText>(R.id.bmiValue)
        val result:TextView = findViewById<EditText>(R.id.result)
        loginButton.setOnClickListener{
            var nameInput:String =""
            var ageInput:Int = 0
            var weightInput:Float = 0.0F
            var heightFInput:Float= 0.0F
            var heightIInput: Float= 0.0F
            var bmi:Float= 0.0F

            try {
                nameInput = name.text.toString().trim()
                ageInput = age.text.toString().toInt()
                weightInput = weight.text.toString().toFloat()
                heightFInput = heightF.text.toString().toFloat()
                heightIInput = heightI.text.toString().toFloat()
                bmi = calculateBMI(weightInput,heightFInput,heightIInput)

                database = FirebaseDatabase.getInstance()
                databaseReference = database.getReference("user")
                val userInfo = UserInfo(nameInput,ageInput,weightInput,heightFInput,heightIInput,bmi)
                databaseReference.child(nameInput).setValue(userInfo).addOnSuccessListener {
                    name.text.clear()
                    age.text.clear()
                    weight.text.clear()
                    heightF.text.clear()
                    heightI.text.clear()
                    Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failure!",Toast.LENGTH_SHORT).show()
                }
            }
            catch(_:java.lang.Exception){
                Toast.makeText(this,"Check Values Entered!",Toast.LENGTH_SHORT).show()
                name.text.clear()
                age.text.clear()
                weight.text.clear()
                heightF.text.clear()
                heightI.text.clear()
            }

            val formattedBMI = String.format("%.2f", bmi).toFloat()
            when {
                formattedBMI < 18.5 -> {
                    result.text = "Underweight"
                }
                formattedBMI < 25 -> {
                    result.text = "Normal weight"
                }
                formattedBMI < 30 -> {
                    result.text = " Overweight"
                }
                formattedBMI <34.9 -> {
                    result.text = "Class I obese"
                }
                formattedBMI <39.9-> {
                    result.text = "Class II obese"
                }
                else -> {
                    result.text = "Class III obese"
                }

            }
            bmiValue.text = "Your BMI is $formattedBMI"
        }
        val historyButton = findViewById<Button>(R.id.history_button)
        historyButton.setOnClickListener{
            val intent = Intent(this,History::class.java)
            startActivity(intent)


        }
    }

    private fun calculateBMI(weightInput: Float, heightFInput: Float, heightIInput: Float): Float {
        val heightInchesTotal = heightIInput + (heightFInput * 12)
        return 703 * (weightInput / (heightInchesTotal * heightInchesTotal))
    }
}
