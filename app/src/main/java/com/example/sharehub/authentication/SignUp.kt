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
import com.example.sharehub.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUp : AppCompatActivity() {
    private lateinit var name : String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var conPassword : String
    private lateinit var confirmedPassword :String
    //view identification
    private lateinit var inputName : EditText
    private lateinit var inputEmail : EditText
    private lateinit var inputPassword : EditText
    private lateinit var inputConPassword : EditText


    //firebase variables
    private lateinit var auth: FirebaseAuth
    private  var firebaseStore: FirebaseStorage? =null
    private var storageReference: StorageReference?=null
    private var databaseReference: DatabaseReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //initializing auth
        auth = Firebase.auth

        //firebase product initialization
        auth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference("UsersPics")
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        //databaseReference = Firebase.database.reference
        //view identification
        inputName = findViewById(R.id.inputName)
        inputEmail = findViewById<EditText>(R.id.inputEmail)
        inputPassword = findViewById<EditText>(R.id.inputPassword)
        inputConPassword = findViewById<EditText>(R.id.inputConfirmPassword)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        tvLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener{
            getInput()
        }
    }
    private fun getInput(){
        name =  inputName.text.toString()
        email = inputEmail.text.toString()
        password = inputPassword.text.toString()
        conPassword = inputConPassword.text.toString()
        if (password == conPassword){
            confirmedPassword = password
            //function to register to firebase and realtime database
            registerToFirebase(name,email,confirmedPassword)

        }else{
            Toast.makeText(applicationContext,"Passwords don't match!!!",Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUi() {
        val intent = Intent(applicationContext,LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerToFirebase(name: String, email: String, confirmedPassword: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    //adding user to realtime database
                    val user = User(name,email)
                    val userId = task.result?.user?.uid
                    if (userId != null) {
                        databaseReference?.child(userId)?.setValue(user)?.addOnSuccessListener {
                            Log.d(TAG,"Added user to database successfully!!!")
                        }
                    }
                    Toast.makeText(baseContext, "Registration successful",
                        Toast.LENGTH_SHORT).show()

                    updateUi()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Registration failed. "+ task.exception?.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}