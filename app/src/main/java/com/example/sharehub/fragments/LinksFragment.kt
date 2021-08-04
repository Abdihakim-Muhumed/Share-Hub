package com.example.sharehub.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharehub.R
import com.example.sharehub.adapters.LinkListAdapter
import com.example.sharehub.models.LinksModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.perfmark.Link
import kotlinx.android.synthetic.main.fragment_links.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LinksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LinksFragment(context: Activity, private val id: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_links, container, false)
        val linkList = mutableListOf<LinksModel>()
        val roomId = id
        val databaseReferenceLinks = FirebaseDatabase.getInstance().getReference("links")
        val databaseReference = FirebaseDatabase.getInstance().getReference("share_rooms")
        databaseReference.child(roomId).child("links").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    val linkId= h.value.toString()
                    databaseReferenceLinks.child(linkId).addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val link = snapshot.getValue(LinksModel::class.java)
                            if (link != null) {
                                Log.d("linksfetching","link found : " + link.title)
                                linkList.add(link)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        if (linkList.size>0){
            val tittles = mutableListOf<String>()
            for (link in linkList){
                tittles.add("title")
            }
            val myListAdapter = LinkListAdapter(context as Activity,tittles,linkList)
            listviewLinks.adapter = myListAdapter
        }else{
            textviewLinks.text = "Links when added will appear here!"
        }

    }

}