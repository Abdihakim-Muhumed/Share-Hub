package com.example.sharehub.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.sharehub.Home
import com.example.sharehub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        val tvSignup: TextView = findViewById(R.id.tvSignup)
        val tvForgot = findViewById<TextView>(R.id.tvForgot)
        tvSignup.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        tvForgot.setOnClickListener{
            val intentForgot = Intent(this,PasswordReset::class.java)
            startActivity(intentForgot)
        }
        //view identification
        val btnLogin = findViewById<Button>(R.id.cirLoginButton)
        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()

            //siginig in user to firbase
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUi(user)
                        Toast.makeText(baseContext, "Authentication Successful!.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed!",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(applicationContext,Home::class.java)
        startActivity(intent)
    }

}