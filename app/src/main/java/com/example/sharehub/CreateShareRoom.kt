package com.example.sharehub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sharehub.models.ShareRoomModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.layout_create_share_room.*

class CreateShareRoom : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private  var firebaseStore: FirebaseStorage?=null
    private var storageReference:StorageReference?=null
    private var databaseReference: DatabaseReference?=null
    private var databaseReferenceUser: DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_share_room)
        //firebase product initialization
        auth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference("imageResources")
        databaseReference = FirebaseDatabase.getInstance().getReference("share_rooms")
        //databaseReference = Firebase.database.reference
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users")
        val btnCreate = findViewById<Button>(R.id.btnCreate)
        btnCreate.setOnClickListener {
            val title = findViewById<EditText>(R.id.editTextTitle).text.toString()
            val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
            if (title.isEmpty()){
                editTextTitle.error = "Title cannot be empty!!!"
                return@setOnClickListener
                Toast.makeText(applicationContext, "Title cannot be empty!",Toast.LENGTH_LONG).show()
            }else{
                //to create a share room in the realtime database
                createShareRoom(title,description)
            }
        }
    }
    private fun createShareRoom(title: String, description: String) {
        //val currentUser= auth.currentUser
        val currentUid = auth.currentUser?.uid

        //writing to realtime database
        val roomId = databaseReference?.push()?.key
        val shareRoom = ShareRoomModel(roomId,title,description,currentUid)
        if (roomId != null) {
            databaseReference?.child(roomId)?.setValue(shareRoom)?.addOnCompleteListener {
                Toast.makeText(applicationContext,"Share Room creation successful!",Toast.LENGTH_LONG).show()
            }
        }
        //adding member to share_room in database
        if (currentUid != null) {
            if (roomId != null) {
                //adding share_room to user in database
                databaseReferenceUser?.child(currentUid)?.child("rooms")?.child(roomId)?.setValue(roomId)
            }
        }


        val intent = Intent(applicationContext,ShareRoom::class.java).apply {
            putExtra("roomId",roomId.toString())
        }
        startActivity(intent)
    }
    /*private fun randomCode(): String = List(8) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")*/
}