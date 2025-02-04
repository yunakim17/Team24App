package com.example.team24app

import java.io.Serializable

// home의 리사이클러뷰를 위한 데이터 클래스
data class Post(
    val profile : String,
    val id : String,
    val post_id : Int,
    val picture : String,
    var like : Int,
    val comment : String,
    val date : String,
    val hour : Int,
    val minute : Int,
    val second : Int,
    val isClicked : Boolean
) : Serializable
