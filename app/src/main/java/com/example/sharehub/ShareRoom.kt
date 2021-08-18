package com.example.sharehub

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sharehub.adapters.FragmentsAdapter
import com.example.sharehub.adapters.LinkListAdapter
import com.example.sharehub.adapters.PagerAdapter
import com.example.sharehub.authentication.LoginActivity
import com.example.sharehub.models.LinksModel
import com.example.sharehub.models.ShareRoomModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_share_room.*
import kotlinx.android.synthetic.main.add_link_dialog.*

class ShareRoom : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    //var tabLayout: TabLayout? = null
    //var viewPager: ViewPager? = null
    private lateinit var shareRoom: ShareRoomModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth: FirebaseAuth
    private  var firebaseStore: FirebaseStorage?=null
    private var storageReference: StorageReference?=null
    private var databaseReference: DatabaseReference?=null
    private var databaseReferenceUser: DatabaseReference?=null
    private var databaseReferenceLinks: DatabaseReference?=null
    val roomId : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_room)
        //firebase product initialization
        auth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference("imageResources")
        databaseReferenceLinks = FirebaseDatabase.getInstance().getReference("links")
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users")
        //navigationview component initialization.
        bottomNavigationView = findViewById(R.id.bottomNAv)
        bottomNavigationView.setOnItemSelectedListener(this)

        shareRoom = ShareRoomModel("1","Sample Share Room","This is a sample share room and it does not represent a real share room","1")
        //getting room id from intent
        val roomId = intent.getStringExtra("roomId").toString()
        Log.d("roomIdFromIntent","roomId " +roomId+" found ")

        //getting room from database
        val databaseReference = FirebaseDatabase.getInstance().getReference("share_rooms")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                loop@ for (h in snapshot.children) {
                    val room =  h.getValue(ShareRoomModel::class.java)
                    val id = room?.id
                    if(id == roomId){
                        shareRoom = room
                        Log.d("ShareRoomFound","Shareroom "+shareRoom.title+" found.")
                        roomTitle.text = shareRoom.title
                        break@loop
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //Fetching resources links
        val linkList = mutableListOf<LinksModel>()
        val databaseReferenceLinks = FirebaseDatabase.getInstance().getReference("links")
        databaseReferenceLinks.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                linkList.clear()
                for (h in snapshot.children){
                    val link = h.getValue(LinksModel::class.java)
                    if (link != null) {
                        if (link.roomId == roomId){
                            Log.d("linksFetching","link found : " + link.title)
                            linkList.add(link)
                        }
                    }
                }

                Log.d("linkList","found links in the list")
                val tittles = mutableListOf<String>()
                for (link in linkList){
                    tittles.add("title")
                }
                //displaying links using list view
                val myListAdapter = LinkListAdapter(this@ShareRoom,tittles,linkList)
                Log.d("adapter","adapter object created")
                val listviewLinks = findViewById<ListView>(R.id.listviewLinks)
                listviewLinks.adapter = myListAdapter
                Log.d("listAdapter","Adapter set!!!")

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //displaying resource links
            //setting static links
        val link1 = LinksModel("2","Link1","https://www.javatpoint.com/kotlin-android-custom-listview","2",roomId)
        val link2 = LinksModel("3","Link2","https://www.tutorialspoint.com/index.htm","2",roomId)
        val link3 = LinksModel("4","Link3","https://www.tutorialspoint.com/index.htm","2",roomId)
        val link4 = LinksModel("5","Link4","https://www.tutorialspoint.com/index.htm","2",roomId)
        val link5 = LinksModel("6","Link5","www.com","2",roomId)
        linkList.add(link1)
        linkList.add(link2)
        linkList.add(link3)
        linkList.add(link4)
        linkList.add(link5)

    }

    //bottom menu items on selected
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuHome -> {
                val intentHome = Intent(this, Home::class.java)
                startActivity(intentHome)
            }
            R.id.menuAddResource -> {
                showLinkDialog()
            }
            R.id.menuLogout -> {
                Firebase.auth.signOut()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    //dialog to add new resource links
    private fun showLinkDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_link_dialog, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()
        val btnDismiss = dialogView.findViewById<Button>(R.id.btnCancelLink)
        val editTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editLink = dialogView.findViewById<EditText>(R.id.editTextLink)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmitLink)
        btnDismiss.setOnClickListener {
            customDialog.dismiss()
        }
        btnSubmit.setOnClickListener {
            val title = editTitle.text.toString()
            val link = editLink.text.toString()
            val uid = auth.currentUser?.uid
            if (title.isEmpty()){
                Toast.makeText(applicationContext,"Title cannot be empty!!!",Toast.LENGTH_LONG).show()
            }
            else if (link.isEmpty()){
                Toast.makeText(applicationContext,"Provide the link!!!",Toast.LENGTH_LONG).show()
            }
            else{
                val linkId = databaseReferenceLinks?.push()?.key
                if (linkId != null) {
                    //getting room id from intent
                    val roomId = intent.getStringExtra("roomId").toString()
                    Log.d("roomIdFromIntent","roomId " +roomId+"found ")
                    val linkModel = uid?.let { it1 -> LinksModel(linkId,title,link, it1,roomId) }
                    if (linkModel != null) {
                        addLink(linkModel,linkId)
                    }
                    customDialog.dismiss()
                }
            }
        }
    }

    //function to add link to database
    private fun addLink(linkModel: LinksModel, linkId: String?){
        if (linkId != null) {
            databaseReferenceLinks?.child(linkId)?.setValue(linkModel)?.addOnSuccessListener {
                Log.d("AddLink","Link added")
                //Toast.makeText(applicationContext,"Link Added!!!",Toast.LENGTH_LONG).show()
                Toast.makeText(applicationContext,"Link added to the room",Toast.LENGTH_LONG).show()

            }
        }
    }
}



//viewpager2
/*val words = arrayListOf("One",)
val pagerAdapter = PagerAdapter(this,words,linkList)
viewPager.adapter = pagerAdapter
viewPager.offscreenPageLimit = 3
TabLayoutMediator(tabLayout, viewPager) { tab, position ->
    tab.text = "${position + 1}"
    if (position==0){
        tab.text = "Links"
    }
}.attach()*/


//viewpager
/*val fragmentAdapter = FragmentsAdapter(this,supportFragmentManager,shareRoom)
if (fragmentAdapter!=null){
    Log.d("FragmentsAdapter","adapter created!!")
}*/