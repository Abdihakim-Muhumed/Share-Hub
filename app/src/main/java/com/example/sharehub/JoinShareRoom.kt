package com.example.sharehub

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JoinShareRoom : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var  databaseReference: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_share_room)
        //share pref initialization
        sharedPreferences = getSharedPreferences("shareRoomPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        //firebase product initialization
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("share_rooms")
        //btn to join a room
        val btnJoinRoom = findViewById<Button>(R.id.btnJoinRoom)
        btnJoinRoom.setOnClickListener {
            val roomCode = findViewById<EditText>(R.id.editTextRoomCode).text.toString()
            val currentUid = auth.currentUser?.uid
            if (roomCode.isEmpty()){
                Toast.makeText(applicationContext,"Enter Room Code !",Toast.LENGTH_LONG).show()

            }else{
                val roomId = databaseReference.child(roomCode).get()
                if (roomId.isSuccessful){
                    if (currentUid != null) {
                        //databaseReference?.child(roomCode)?.child("members")?.child(currentUid)?.setValue(currentUid)
                        //adding room id to user profile
                        val databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users")
                        databaseReferenceUser?.child(currentUid)?.child("rooms")?.child(roomId.toString())?.setValue(roomId)
                        //opening the share room acrivity
                        val intent = Intent(applicationContext,ShareRoom::class.java).apply {
                            putExtra("roomId",roomCode)
                        }
                        startActivity(intent)
                    }
                }else {
                    Toast.makeText(applicationContext,"Share Room does not exist!!!",Toast.LENGTH_LONG).show()
                }

                //to join a room

            }
        }
        //if user wants to create a room instead of joining one
        val createRoom = findViewById<TextView>(R.id.tvCreateRoom)
        createRoom.setOnClickListener {
            val intentCreateRoom = Intent(this, CreateShareRoom::class.java)
            startActivity(intentCreateRoom)
        }
    }
}