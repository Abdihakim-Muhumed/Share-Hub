package com.example.sharehub.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.example.sharehub.R
import com.example.sharehub.models.LinksModel

class LinkListAdapter(private val context: Activity, private val title: MutableList<String>,private val links: MutableList<LinksModel>)
    : ArrayAdapter<String>(context, R.layout.links_list_layout,title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.links_list_layout, null, true)
        Log.d("inflater","layout inflated")

        val imgid:Int = R.drawable.url_icon
        val titleText = rowView.findViewById(R.id.title) as TextView
        //val imageView = rowView.findViewById(R.id.icon_links) as ImageView
        val subtitleText = rowView.findViewById(R.id.link) as TextView

        titleText.text = links[position].title
        //imageView.setImageResource(imgid)
        subtitleText.text = links[position].link

        val layoutLinkInfo = rowView.findViewById<LinearLayout>(R.id.layoutLinkInfo)

        layoutLinkInfo.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse(links[position].link)
            context.startActivity(openURL)
        }

        return rowView
    }
}
