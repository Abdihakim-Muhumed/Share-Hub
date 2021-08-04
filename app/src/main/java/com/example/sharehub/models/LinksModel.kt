package com.example.sharehub.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class LinksModel(val id: String?=null, val title:String?=null, val link: String?=null, val sharingUid: String?=null,val roomId:String?=null) {

}
