package com.example.sharehub.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sharehub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordReset : AppCompatActivity() {
    //private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)
        val btnReset = findViewById<Button>(R.id.btnReset)
        btnReset.setOnClickListener {
            email = findViewById<EditText>(R.id.resetTextEmail).text.toString()
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        emailSent()
                    }else{
                        Toast.makeText(applicationContext, "Email does not exist!!!",Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun emailSent() {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //setting background

        //performing positive action
        builder.setPositiveButton("OK"){dialogInterface, which ->
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_corner_blue)
        alertDialog.show()
    }
}