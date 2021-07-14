package com.example.sharehub.models

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class ShareRoomModel(val id:String?=null,val title:String?=null,val description:String?=null,val roomMasterId : String?=null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}