package com.example.sharehub.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sharehub.Home
import com.example.sharehub.R
import com.example.sharehub.ShareRoom
import com.example.sharehub.models.ShareRoomModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_roomcards.view.*

class RecyclerViewAdapterRooms(private val context: Context,private val roomList: MutableList<ShareRoomModel?>): RecyclerView.Adapter<RecyclerViewAdapterRooms.ViewHolder>() {
    private val  auth = FirebaseAuth.getInstance()
    //val share_rooms = databaseReference.
    private val images = R.drawable.share_room_icon

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var roomImage: ImageView
        var roomTitle: TextView
        var roomDetail: TextView
        var copyCode: Button
        var layoutInfo: LinearLayout
        var btnCopyRoomCode: Button

        init {
            roomImage = itemView.findViewById(R.id.room_image)
            roomTitle = itemView.findViewById(R.id.room_title)
            roomDetail = itemView.findViewById(R.id.room_master)
            copyCode = itemView.findViewById(R.id.btnCopyCode)
            layoutInfo = itemView.findViewById(R.id.roomInfo_layout)
            btnCopyRoomCode = itemView.findViewById<Button>(R.id.btnCopyCode)

        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_roomcards, viewGroup, false)

        Log.d("viewholdercreation","Viewholder was created!")
        /*//button copy room code
        val btnCopyRoomCode = v.findViewById<Button>(R.id.btnCopyCode)
        btnCopyRoomCode.setOnClickListener {
            //code to copy room code
            val code = roomList[i]?.id
            //copyCodeToClipboard(code)
            val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip: ClipData = ClipData.newPlainText("Code copied", code)
            myClipboard.setPrimaryClip(myClip)
            Toast.makeText(context,"Room code copied to clipboard!",Toast.LENGTH_LONG).show()
        }*/

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val room = roomList[i]
        if (room != null) {
            viewHolder.roomTitle.text = room.title
            viewHolder.roomDetail.text = room.description

            //openning room when clicked
            viewHolder.layoutInfo.setOnClickListener {
                val intent = Intent(context,ShareRoom::class.java).apply {
                    putExtra("roomId",room.id)
                }
                context.startActivity(intent)
            }
            //copying code
            val btnCopyRoomCode: Button = viewHolder.copyCode
            btnCopyRoomCode.setOnClickListener {
                //code to copy room code
                val code = roomList[i]?.id
                //copyCodeToClipboard(code)
                val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClip: ClipData = ClipData.newPlainText("Code copied", code)
                myClipboard.setPrimaryClip(myClip)
                Toast.makeText(context,"Room code copied to clipboard!",Toast.LENGTH_LONG).show()
            }

            viewHolder.copyCode.text = "Copy Code"
            viewHolder.roomImage.setImageResource(images)

        }





    }

    override fun getItemCount(): Int {
        return roomList.size
    }

}