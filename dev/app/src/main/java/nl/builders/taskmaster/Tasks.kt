package com.example.taksmasterapp
data class Tasks(
        val ranking: Double?=-1.0,
        val storageID:String?="",
        val description:String?="",
        val userID:String?="",
        val userUID:String?=""// get from tasklist when clicked on in listview
)