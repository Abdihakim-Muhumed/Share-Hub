package com.example.sharehub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.sharehub.adapters.LinkListAdapter
import com.example.sharehub.models.LinksModel

class CustomListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_list_view)

        val link = LinksModel("2","Link1","www.google.com","2","dfghkb")
        val linkList = mutableListOf<LinksModel>()
        linkList.add(link)
        linkList.add(link)
        Log.d("linkList","found links in the list")
        val tittles = mutableListOf<String>()
        for (link in linkList){
            tittles.add("title")
        }
        val myListAdapter = LinkListAdapter(this@CustomListView,tittles,linkList)
        Log.d("adapter","adapter object created")
        val listviewLinks = findViewById<ListView>(R.id.listviewLinks)
        listviewLinks.adapter = myListAdapter
        Log.d("listAdapter","Adapter set!!!")

    }
}