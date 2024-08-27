package com.example.stocktracker

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FPersonalInformation(var id:Int?=null,var mail:String?=null,var username:String?=null,var password:String?=null) {
}