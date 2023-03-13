package com.example.fingerprintstudentverification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnAdd=findViewById<Button>(R.id.btnAddStudent)
        val btnverify=findViewById<Button>(R.id.btnverify)
        btnAdd.setOnClickListener {
            startActivity(Intent(this,AddStudents::class.java))
        }
        btnverify.setOnClickListener {
            startActivity(Intent(this,VerifyStudent::class.java))
        }
    }
}