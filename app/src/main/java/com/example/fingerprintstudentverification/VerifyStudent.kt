package com.example.fingerprintstudentverification

import User
import android.Manifest
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerifyStudent : AppCompatActivity() {
    private var regno: String? =null
    private var cancellationSignal: CancellationSignal? = null
    private val  authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Fingerprint Capture error: $errString")
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                   val prb:ProgressBar=findViewById(R.id.prbar)
                    prb.visibility=View.VISIBLE
                    regno=findViewById<TextInputEditText>(R.id.txtRegno).text.toString()
                    val db=FirebaseDatabase.getInstance()
                    val ref=db.reference
                    val txtreg:TextInputEditText=findViewById(R.id.txtRegno)
                    txtreg.isEnabled=false
                    ref.child("Students").addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val fees=snapshot.child(regno.toString()).child("feesPayment").getValue()

                            Toast.makeText(this@VerifyStudent,fees.toString(),Toast.LENGTH_LONG).show()
                            if(fees== "paid")
                            {
                                startActivity(Intent(this@VerifyStudent,success::class.java))
                            }
                            else if (fees== "not paid")
                            {
                                startActivity(Intent(this@VerifyStudent,error::class.java))
                            }
                            else if(fees==null){
                                startActivity(Intent(this@VerifyStudent,notfound::class.java))
                            }
                            prb.visibility=View.GONE
                            txtreg.isEnabled=true
                            findViewById<TextInputEditText>(R.id.txtRegno).text?.clear()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
               }
            }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_student)
        val verifyBtn: Button =findViewById(R.id.btnVerifyStudent)
        verifyBtn.setOnClickListener {
            val biometricPrompt : BiometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Student Verification")
                .setSubtitle("Fingerprint is required")
                .setDescription("Verify fingerprint to access your details")
                .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                }).build()
            biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
        }
    }

    private fun notifyUser(message: String) {
        //Toast.makeText(this@VerifyStudent, message, Toast.LENGTH_SHORT).show()
    }
    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

}