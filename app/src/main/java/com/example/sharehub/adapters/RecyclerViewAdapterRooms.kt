package com.example.sharehub.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.sharehub.Home
import com.example.sharehub.R
import com.example.sharehub.models.ShareRoomModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewAdapterRooms(private val context: Context,private val roomList: MutableList<ShareRoomModel>): RecyclerView.Adapter<RecyclerViewAdapterRooms.ViewHolder>() {
    private val  auth = FirebaseAuth.getInstance()
    val databaseReference = FirebaseDatabase.getInstance().getReference("share_rooms")
    val currentUid = auth.currentUser?.uid
    //val share_rooms = databaseReference.
    private val titles = arrayOf("Share Room One",
        "Share Room Two", "Share Room Three", "Share Room Four",
        "Share Room Five", "Share Room Six", "Share Room Seven",
        "Share Room Eight")

    private val details = arrayOf("Share Room one details", "Share Room two details",
        "Share Room three details", "Share Room four details",
        "Share Room file details", "Share Room six details",
        "Share Room seven details", "Share Room eight details")

    private val images = R.drawable.share_room_icon

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var roomImage: ImageView
        var roomTitle: TextView
        var roomDetail: TextView
        var copyCode: Button

        init {
            roomImage = itemView.findViewById(R.id.room_image)
            roomTitle = itemView.findViewById(R.id.room_title)
            roomDetail = itemView.findViewById(R.id.room_master)
            copyCode = itemView.findViewById(R.id.btnCopyCode)
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_roomcards, viewGroup, false)

        //button copy room code

        val btnCopyRoomCode = v.findViewById<Button>(R.id.btnCopyCode)
        btnCopyRoomCode.setOnClickListener {
            //code to copy room code
            val code = roomList[i].id
            //copyCodeToClipboard(code)
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val room = roomList[i]
        viewHolder.roomTitle.text = room.title
        viewHolder.roomDetail.text = room.description
        viewHolder.copyCode.text = "Copy Code"
        viewHolder.roomImage.setImageResource(images)

    }

    override fun getItemCount(): Int {
        return roomList.size
    }

}