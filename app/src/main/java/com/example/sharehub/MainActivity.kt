package com.example.sharehub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.sharehub.authentication.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            if (Firebase.auth.currentUser !=null){
                val intentHome = Intent(this,Home::class.java)
                startActivity(intentHome)
            }else{
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        },1500)
    }
}