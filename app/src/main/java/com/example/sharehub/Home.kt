package com.example.sharehub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharehub.adapters.RecyclerViewAdapterRooms
import com.example.sharehub.authentication.LoginActivity
import com.example.sharehub.models.ShareRoomModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_roomcards.*

class Home : AppCompatActivity() , BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerViewAdapterRooms?=null
    private lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //firebase product initialization
        auth = FirebaseAuth.getInstance()
        val databaseReference = Firebase.database.reference
        val databaseReferenceRooms = FirebaseDatabase.getInstance().getReference("share_rooms")
        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNAv)
        bottomNavigationView.setOnItemSelectedListener(this)

        //fetching share rooms and displaying them
        val roomIds = mutableListOf<String?>()
        val roomList = mutableListOf<ShareRoomModel?>()
        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            databaseReference.child("users").child(currentUid).child("rooms").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        //fetching roomIds from the user
                        for (h in snapshot.children){
                            val roomId = h.value.toString()
                            Log.d("roomId","Room id found: "+roomId)
                            roomIds.add(roomId)
                        }
                        //using roomIds to fetch rooms
                        for (roomId in roomIds){
                            if (roomId != null) {
                                databaseReference.child("share_rooms").child(roomId).addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot!!.exists()){
                                            val room = snapshot.getValue(ShareRoomModel::class.java)
                                            if (room != null) {
                                                Log.d("roomFound","True: "+ room.title)
                                                roomList.add(room)
                                            }
                                        }
                                        //displaying rooms using layout manager
                                        if (roomList.isNotEmpty()){
                                            layoutManager = LinearLayoutManager(this@Home)
                                            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_rooms)
                                            recyclerView.layoutManager = layoutManager
                                            adapter = RecyclerViewAdapterRooms(this@Home,roomList)
                                            Log.d("Adapter","Recyclerviewadapter created successfully!!!")
                                            recyclerView.adapter = adapter
                                        }

                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })
                            }
                            else{
                                Log.d("roomIds","roomIds is empty!")
                                Log.d("roomList","roomList is empty!!")
                                homeTitle.text = "No Share room joined yet!"
                            }

                        }

                    }


                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        }

    }

    //bottom navigation when a menu item is selected
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuCreateRoom -> {
                val intentCreateRoom = Intent(this, CreateShareRoom::class.java)
                startActivity(intentCreateRoom)
            }
            R.id.menuJoinRoom -> {
                val intentJoinRoom = Intent(this, JoinShareRoom::class.java)
                startActivity(intentJoinRoom)
            }
            R.id.menuLogout -> {
                Firebase.auth.signOut()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onBackPressed() {
         super.onBackPressed()
        finish()
    }
}