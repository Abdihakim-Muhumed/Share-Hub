package com.example.sharehub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.example.sharehub.adapters.FragmentsAdapter
import com.example.sharehub.authentication.LoginActivity
import com.example.sharehub.models.ShareRoomModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_share_room.*

class ShareRoom : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    //var tabLayout: TabLayout? = null
    //var viewPager: ViewPager? = null
    private lateinit var shareRoom: ShareRoomModel
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_room)

        bottomNavigationView = findViewById(R.id.bottomNAv)
        bottomNavigationView.setOnItemSelectedListener(this)

        shareRoom = ShareRoomModel("1","Sample Share Room","This is a sample share room and it does not represent a real share room","1")
        //getting room id from intent
        val roomId = intent.getStringExtra("roomId").toString()
        Log.d("roomIdFromIntent","roomId " +roomId+"found ")
        //getting room from database
        val databaseReference = FirebaseDatabase.getInstance().getReference("share_rooms")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children) {
                    val room =  snapshot.getValue(ShareRoomModel::class.java)
                    val id = room?.id
                    if(id == roomId){
                        shareRoom = room
                        Log.d("ShareroomFound","Shareroom "+shareRoom.title+" found.")
                        break
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val fragmentAdapter = FragmentsAdapter(this,supportFragmentManager,shareRoom)
        if (fragmentAdapter!=null){
            Log.d("FragmentsAdapter","adapter created!!")
        }
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuHome -> {
                val intentHome = Intent(this, Home::class.java)
                startActivity(intentHome)
            }
            R.id.menuAddResource -> {
            }
            R.id.menuLogout -> {
                Firebase.auth.signOut()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}