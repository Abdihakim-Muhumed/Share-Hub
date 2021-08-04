package com.example.sharehub.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sharehub.R
import com.example.sharehub.fragments.LinksFragment
import com.example.sharehub.models.LinksModel
import kotlinx.android.synthetic.main.fragment_links.*

class PagerAdapter(private val context: Context, private val words: List<String>,val linkList: MutableList<LinksModel>): RecyclerView.Adapter<PagerAdapter.PageHolder>(){
    inner class PageHolder(view: View): RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById<TextView>(R.id.textviewLinks)
        val listviewLinks = view.findViewById<ListView>(R.id.listviewLinks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder =
        PageHolder(LayoutInflater.from(context).inflate(R.layout.fragment_links, parent, false))


    override fun onBindViewHolder(holder: PageHolder, position: Int) {
        holder.textView.text = words[position]
        if (linkList.size>0){
            val tittles = mutableListOf<String>()
            for (link in linkList){
                tittles.add("title")
            }
            val myListAdapter = LinkListAdapter(context as Activity,tittles,linkList)
            val listviewLinks = holder.listviewLinks
            listviewLinks.adapter = myListAdapter
        }else{
            holder.textView.text = "Links when added will appear here!"
        }


    }

    override fun getItemCount(): Int = words.size

}