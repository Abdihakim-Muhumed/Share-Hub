package com.example.sharehub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharehub.adapters.RecyclerViewAdapterRooms
import com.example.sharehub.models.ShareRoomModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_roomcards.*

class Home : AppCompatActivity() , BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerViewAdapterRooms.ViewHolder>? = null
    private lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //firebase product initialization
        auth = FirebaseAuth.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val databaseReferenceRooms = FirebaseDatabase.getInstance().getReference("share_rooms")
        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNAv)
        bottomNavigationView.setOnItemSelectedListener(this)

        //fetching share rooms
        val roomList = mutableListOf<ShareRoomModel>()
        val currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            databaseReference.child(currentUid).child("rooms").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children){
                        val roomId = h.value.toString()
                        databaseReferenceRooms.child(roomId).addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                               val room = snapshot.getValue(ShareRoomModel::class.java)
                                if (room != null) {
                                    roomList.clear()
                                    roomList.add(room)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
                    //layout manager
                    layoutManager = LinearLayoutManager(this@Home,)
                    val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_rooms)
                    recyclerView.layoutManager = layoutManager
                    adapter = RecyclerViewAdapterRooms(this@Home,roomList)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        /*room_layout.setOnClickListener{

        }*/

    }

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
        }
        return true
    }
}

