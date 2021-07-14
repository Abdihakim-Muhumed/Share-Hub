package com.example.sharehub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ShareRoom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_room)
        //getting data from intent
        val intentValue = intent.getStringExtra("roomId")
    }
}